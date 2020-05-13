/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.jellyfish.service.impl.templateservice;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.jellyfish.service.property.api.IProperties;
import com.ngc.seaside.jellyfish.service.property.api.IPropertyService;
import com.ngc.seaside.jellyfish.service.template.api.DefaultTemplateOutput;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateOutput;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.service.template.api.TemplateServiceException;

import org.apache.commons.io.IOUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Default implementation of the {@link ITemplateService} interface.
 */
@Component(service = ITemplateService.class)
public class TemplateService implements ITemplateService {

   private static final String TEMPLATES_DIRECTORY = "templates";

   private static final String TEMPLATE_FOLDER = "templateContent";
   private static final String TEMPLATE_PROPERTIES = "template.properties";
   private static final String TEMPLATE_NAME_ENDING_FORMAT = "-template%s.zip";

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
   public boolean templateExists(String templatePrefix) {
      InputStream is;
      try {
         is = getTemplateInputStream(templatePrefix);
         is.close();
      } catch (IOException | TemplateServiceException e) {
         // Don't care about any exception, this just means the template may not exists.
         is = null;
      }
      return is != null;
   }

   @Override
   public ITemplateOutput unpack(String templateName,
                                 IParameterCollection parameters,
                                 Path outputDirectory,
                                 boolean clean)
         throws TemplateServiceException {
      ITemplateOutput output;

      try (ZipInputStream zis = new ZipInputStream(getTemplateInputStream(templateName))) {
         Path unzippedFolderPath = Files.createTempDirectory(null);
         ZipEntry entry = zis.getNextEntry();

         while (entry != null) {
            File entryDestination = new File(unzippedFolderPath.toString(), entry.getName());
            if (entry.isDirectory()) {
               entryDestination.mkdirs();
            } else {
               entryDestination.getParentFile().mkdirs();
               OutputStream out = new FileOutputStream(entryDestination);
               IOUtils.copy(zis, out);
               zis.closeEntry();
               out.close();
            }
            entry = zis.getNextEntry();
         }

         if (!isValidateTemplate(unzippedFolderPath)) {
            String message = String.format(
                  "Invalid template. Each template must contain %s and a template folder named '%s'",
                  TEMPLATE_PROPERTIES,
                  TEMPLATE_FOLDER);
            logService.error(getClass(), message);
            throw new TemplateServiceException(message);
         }

         TemplateIgnoreComponent templateIgnoreComponent = new TemplateIgnoreComponent(unzippedFolderPath,
                                                                                       TEMPLATE_FOLDER,
                                                                                       logService);
         templateIgnoreComponent.parse();

         output = updateTemplate(unzippedFolderPath,
                                 parameters,
                                 outputDirectory,
                                 clean,
                                 templateIgnoreComponent);
      } catch (TemplateServiceException | IOException | NullPointerException e) {
         String message = String.format("An error occurred processing the template zip file: %s", templateName);
         logService.error(getClass(), e, message);
         throw new TemplateServiceException(message, e);
      }

      return output;
   }

   /**
    * Invoked to get the input stream to the template ZIP file.
    *
    * @return the input stream to the template zip file
    * @throws IOException              if the stream could not be opened
    * @throws TemplateServiceException if no template with the given name could be found
    */
   protected InputStream getTemplateInputStream(String templateName) throws IOException,
                                                                            TemplateServiceException {
      Path path = getTemplatePath(templateName);
      if (path == null) {
         throw new TemplateServiceException("no template with the name " + templateName + " was found!");
      }
      return new FileInputStream(path.toFile());
   }

   /**
    * Update the template based on the the visitor pattern. This will replace any Velocity Template
    * parameters with the input values from the properties file.
    *
    * @param templateFolder          the template folder.
    * @param parameters              the parameters that should overwrite any parameter values in the
    *                                template.properties this means that it should
    * @param outputFolder            the output folder.
    * @param clean                   true if this should clean existing directories.
    * @param templateIgnoreComponent used to check files that should be copied instead of evaluated by velocity.
    */
   protected ITemplateOutput updateTemplate(Path templateFolder,
                                            IParameterCollection parameters,
                                            Path outputFolder,
                                            boolean clean,
                                            TemplateIgnoreComponent templateIgnoreComponent)
         throws IOException {
      // Parse template.properties file for each parameter and its default value
      IProperties parametersAndDefaults = propertyService.load(templateFolder.resolve(TEMPLATE_PROPERTIES));

      // For each parameter query the user for its value if that property isn't already in the parameters collection.
      Map<String, Object> parametersAndValues = new HashMap<>();
      for (String parameter : parametersAndDefaults.getKeys()) {
         Object value;
         if (parameters.containsParameter(parameter)) {
            // if the value is already passed in by the user, don't ask them for it again.
            value = parameters.getParameter(parameter).getValue();
         } else {
            String defaultValue = parametersAndDefaults.get(parameter);
            value = promptUserService.prompt(parameter, defaultValue, null);
         }

         if (!parametersAndDefaults.get(parameter).equals(value)) {
            parametersAndDefaults.put(parameter, value.toString());
            parametersAndDefaults.evaluate();
         }

         parametersAndValues.put(parameter, value);
      }

      // Insert any remaining parameters into the map so they are available to Velocity.
      // If the parameter is not already in the map, it must have not been referenced in the property value.
      // Even if that is the case, we still want to provide it.
      parameters.getAllParameters().forEach(p -> parametersAndValues.putIfAbsent(p.getName(), p.getValue()));

      TemplateVisitor visitor = new TemplateVisitor(parametersAndValues,
                                                    templateFolder.resolve(TEMPLATE_FOLDER),
                                                    outputFolder,
                                                    clean,
                                                    templateIgnoreComponent);

      // Walk through the unzipped template directory in order to generate the
      // instance of the template
      Files.walkFileTree(templateFolder.resolve(TEMPLATE_FOLDER), visitor);

      return new DefaultTemplateOutput()
            .setOutputPath(visitor.getTopLevelFolder())
            .setProperties(parametersAndValues);
   }

   /**
    * Get the Path object represented by the name of the template. The zip will be in the format of
    * templatePrefix-{version}-template[-{name}].zip
    *
    * @param templatePrefix the prefix of the template. This is usually in the form groupId.artifactId of the command
    *                       being run, optionally with -{name} appended to the end.
    * @return the Path to the template or null if the file doesn't exists or if it is a directory.
    */
   protected Path getTemplatePath(String templatePrefix) throws IOException {
      Path templatesPath = Paths.get(resourceService.getResourceRootPath().toString(), TEMPLATES_DIRECTORY);

      if (Files.isDirectory(templatesPath)) {
         int index = templatePrefix.indexOf('-');
         String templateName = index < 0 ? "" : templatePrefix.substring(index);
         String templatePrefixWithoutName = index < 0 ? templatePrefix : templatePrefix.substring(0, index);
         String templateEnding = String.format(TEMPLATE_NAME_ENDING_FORMAT, templateName);
         Pattern templatePattern = Pattern.compile(
               Pattern.quote(templatePrefixWithoutName) + "-.*?" + Pattern.quote(templateEnding));
         return Files.list(templatesPath)
               .filter(Files::isRegularFile)
               .filter(
                     path -> templatePattern.matcher(path.getFileName().toString()).matches())
               .findAny()
               .orElse(null);
      } else {
         return null;
      }
   }

   /**
    * Determines if the unzipped contents of the template are valid.
    *
    * @param templateFolder path to unzipped template folder
    * @throws TemplateServiceException if the template has an invalid structure/format
    */
   protected boolean isValidateTemplate(Path templateFolder) {
      return Files.exists(templateFolder.resolve(TEMPLATE_PROPERTIES))
            && Files.isRegularFile(templateFolder.resolve(TEMPLATE_PROPERTIES))
            && Files.exists(templateFolder.resolve(TEMPLATE_FOLDER))
            && Files.isDirectory(templateFolder.resolve(TEMPLATE_FOLDER));
   }
}
