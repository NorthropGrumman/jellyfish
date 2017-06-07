package com.ngc.seaside.bootstrap.service.impl.templateservice;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.property.api.IProperties;
import com.ngc.seaside.bootstrap.service.property.api.IPropertyService;
import com.ngc.seaside.bootstrap.service.template.api.DefaultTemplateOutput;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateOutput;
import com.ngc.seaside.bootstrap.service.template.api.TemplateServiceException;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Default implementation of the {@link ITemplateService} interface.
 *
 * @author justan.provence@ngc.com
 */
@Component(service = ITemplateService.class)
public class TemplateService implements ITemplateService {

   private static final String TEMPLATE_FOLDER = "template";
   private static final String TEMPLATE_PROPERTIES = "template.properties";

   private ILogService logService;
   private IResourceService resourceService;
   private IPromptUserService promptUserService;
   private IPropertyService propertyService;

   @Activate
   public void activate() {
     logService.trace(getClass(), "activated");
   }

   @Deactivate
   public void deactivate() {
     logService.trace(getClass(), "deactivated");
   }

   /**
    * Sets log service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   /**
    * Remove log service.
    */
   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   /**
    * Sets the resource service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "removeResourceService")
   public void setResourceService(IResourceService ref) {
      this.resourceService = ref;
   }

   /**
    * Remove the resource service.
    */
   public void removeResourceService(IResourceService ref) {
      setResourceService(null);
   }

   /**
    * Sets the prompt user service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "removePromptUserService")
   public void setPromptUserService(IPromptUserService ref) {
      this.promptUserService = ref;
   }

   /**
    * Remove the prompt user service.
    */
   public void removePromptUserService(IPromptUserService ref) {
      setPromptUserService(null);
   }

   /**
    * Set the property service.
    *
    * @param ref the property service.
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "removePropertyService")
   public void setPropertyService(IPropertyService ref) {
      this.propertyService = ref;
   }

   /**
    * Remove property service.
    *
    * @param ref the property service.
    */
   public void removePropertyService(IPropertyService ref) {
      setPropertyService(null);
   }

   @Override
   public boolean templateExists(String templateName) {
      return getTemplatePath(templateName) != null;
   }

   @Override
   public ITemplateOutput unpack(String templateName, Path outputDirectory, boolean clean)
            throws TemplateServiceException {
      ZipFile zipFile = null;
      ITemplateOutput output;
      try {
         Path path = getTemplatePath(templateName);
         zipFile = new ZipFile(path.toString());
         Path unzippedFolderPath = Files.createTempDirectory(null);
         Enumeration<? extends ZipEntry> entries = zipFile.entries();

         while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            File entryDestination = new File(unzippedFolderPath.toString(), entry.getName());
            if (entry.isDirectory()) {
               entryDestination.mkdirs();
            } else {
               entryDestination.getParentFile().mkdirs();
               InputStream in = zipFile.getInputStream(entry);
               OutputStream out = new FileOutputStream(entryDestination);
               IOUtils.copy(in, out);
               IOUtils.closeQuietly(in);
               out.close();
            }
         }

         if (!isValidateTemplate(unzippedFolderPath)) {
            String message = String.format(
                     "Invalid template. Each template must contain %s and a template folder named '%s'",
                     TEMPLATE_PROPERTIES, TEMPLATE_FOLDER);
            logService.error(getClass(), message);
            throw new TemplateServiceException(message);
         }

         output = updateTemplate(unzippedFolderPath, outputDirectory, clean);

      } catch (TemplateServiceException | IOException e) {
         String message = String.format("An error occurred processing the template zip file: %s", templateName);
         logService.error(getClass(), e, message);
         throw new TemplateServiceException(message, e);
      } finally {
         if (zipFile != null) {
            try {
               zipFile.close();
            } catch (IOException e) {
               //being unable to close the zip file should not kill the process if it has worked
               //up until this point. It will close the file when the application exits.
            }
         }
      }

      return output;
   }

   /**
    * Update the template based on the the visitor pattern. This will replace any Velocity Template
    * parameters with the input values from the properties file.
    *
    * @param templateFolder the template folder.
    * @param outputFolder   the output folder.
    * @param clean          true if this should clean existing directories.
    */
   protected ITemplateOutput updateTemplate(Path templateFolder, Path outputFolder, boolean clean)
            throws IOException {
      // Parse template.properties file for each parameter and its default value
      IProperties parametersAndDefaults =
               propertyService.load(templateFolder.resolve(TEMPLATE_PROPERTIES));

      // For each parameter query the user for its value
      Map<String, String> parametersAndValues = new HashMap<>();
      for (String parameter : parametersAndDefaults.getKeys()) {
         String defaultValue = parametersAndDefaults.get(parameter);
         String value = promptUserService.prompt(parameter, defaultValue, null);

         if (!parametersAndDefaults.get(parameter).equals(value)) {
            parametersAndDefaults.put(parameter, value);
            parametersAndDefaults.evaluate();
         }

         parametersAndValues.put(parameter, value);
      }

      TemplateVisitor visitor = new TemplateVisitor(parametersAndValues,
                          templateFolder.resolve(TEMPLATE_FOLDER),
                          outputFolder,
                          clean);

      // Walk through the unzipped template directory in order to generate the
      // instance of the template
      Files.walkFileTree(templateFolder.resolve(TEMPLATE_FOLDER), visitor);

      return new DefaultTemplateOutput()
               .setOutputPath(visitor.getTopLevelFolder())
               .setProperties(parametersAndValues);
   }

   /**
    * Get the Path object represented by the name of the template. The zip file should be called
    * <pre>Template<templateName>.zip</pre>
    *
    * @param templateName the name of the template.
    * @return the Path to the template or null if the file doesn't exists or if it is a directory.
    */
   protected Path getTemplatePath(String templateName) {
      Path templateFile = buildPath(templateName);
      boolean fileFound = Files.exists(templateFile);

      if (!fileFound || !Files.isRegularFile(templateFile)) {
         return null;
      }

      return templateFile;
   }

   /**
    * Build the path given the template name.
    *
    * @param templateName the template name.
    * @return the Path to the file. notice, this does not check to see if the file exists and should always return a
    * valid template.
    */
   protected Path buildPath(String templateName) {
      return Paths.get(
               resourceService.getResourceRootPath().toString(),
               "templates",
               templateName.toLowerCase(),
               String.format("Template%s.zip", StringUtils.capitalize(templateName)));
   }

   /**
    * Determines if the unzipped contents of the template are valid.
    *
    * @param templateFolder path to unzipped template folder
    * @throws TemplateServiceException if the template has an invalid structure/format
    */
   protected boolean isValidateTemplate(Path templateFolder) {
      return Files.exists(templateFolder.resolve(TEMPLATE_PROPERTIES)) &&
             Files.isRegularFile(templateFolder.resolve(TEMPLATE_PROPERTIES)) &&
             Files.exists(templateFolder.resolve(TEMPLATE_FOLDER)) &&
             Files.isDirectory(templateFolder.resolve(TEMPLATE_FOLDER));
   }
}
