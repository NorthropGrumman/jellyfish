package com.ngc.seaside.bootstrap.service.impl.bootstraptemplateservice;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.template.api.BootstrapTemplateException;
import com.ngc.seaside.bootstrap.service.template.api.IBootstrapTemplateService;
import com.sun.javaws.exceptions.ExitException;
import com.sun.xml.internal.ws.util.StringUtils;

import org.apache.commons.io.IOUtils;

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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author justan.provence@ngc.com
 */
public class BootstrapTemplateServiceDelegate implements IBootstrapTemplateService {

   private static final String TEMPLATE_FOLDER = "template";
   private static final String TEMPLATE_PROPERTIES = "template.properties";

   private ILogService logService;
   private IResourceService resourceService;
   private IPromptUserService promptUserService;

   @Override
   public boolean templateExists(String templateName) {
      return getTemplatePath(templateName) != null;
   }

   @Override
   public Path unpack(String templateName, Path outputDirectory, boolean clean)
            throws BootstrapTemplateException {
      ZipFile zipFile = null;
      Path unzippedFolderPath = null;
      try {
         Path path = getTemplatePath(templateName);
         zipFile = new ZipFile(path.toString());
         unzippedFolderPath = Files.createTempDirectory(null);
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
            throw new BootstrapTemplateException(message);
         }

         updateTemplate(unzippedFolderPath, outputDirectory, clean);

      } catch (BootstrapTemplateException | IOException e) {
         logService.error(getClass(), e,
                          "An error occurred processing the template zip file: %s", templateName);
         throw new BootstrapTemplateException(
                  "An error occurred processing the template zip file: ", e);
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
      return unzippedFolderPath;
   }

   /**
    *
    * @param ref
    */
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
    *
    * @param ref
    */
   public void setPromptUserService(IPromptUserService ref) {
      this.promptUserService = ref;
   }

   /**
    * Remove log service.
    */
   public void removePromptUserService(IPromptUserService ref) {
      setPromptUserService(null);
   }

   /**
    *
    * @param ref
    */
   public void setResourceService(IResourceService ref) {
      this.resourceService = ref;
   }

   /**
    * Remove log service.
    */
   public void removeResourceService(IResourceService ref) {
      setResourceService(null);
   }

   /**
    *
    * @param templateFolder
    */
   protected void updateTemplate(Path templateFolder, Path outputFolder, boolean clean)
            throws IOException {
      // Parse template.properties file for each parameter and its default value
      LinkedHashMap<String, String> parametersAndDefaults =
               parseTemplateProperties(templateFolder.resolve(TEMPLATE_PROPERTIES));

      // For each parameter query the user for its value
      Map<String, String> parametersAndValues = new HashMap<>();
      for (Map.Entry<String, String> entry : parametersAndDefaults.entrySet()) {
         String parameter = entry.getKey();
         String value = promptUserService.prompt(parameter, entry.getValue(), null);
         parametersAndValues.put(parameter, value);
      }

      // Walk through the unzipped template directory in order to generate the
      // instance of the template
      Files.walkFileTree(templateFolder.resolve(TEMPLATE_FOLDER),
                         new BootstrapTemplateVisitor(parametersAndValues,
                                                      templateFolder.resolve(TEMPLATE_FOLDER),
                                                      outputFolder,
                                                      clean));
   }

   /**
    *
    * @param templateName
    * @return
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
    *
    * @param templateName
    * @return
    */
   protected Path buildPath(String templateName) {
      return Paths.get(
               resourceService.getResourceRootPath().toString(),
               "config",
               templateName.toLowerCase(),
               String.format("Template%s", StringUtils.capitalize(templateName)),
               ".zip");
   }

   /**
    * Determines if the unzipped contents of the template are valid.
    *
    * @param templateFolder path to unzipped template folder
    * @throws ExitException if the template has an invalid structure/format
    */
   protected boolean isValidateTemplate(Path templateFolder) {
      return Files.exists(templateFolder.resolve(TEMPLATE_PROPERTIES)) &&
             Files.isRegularFile(templateFolder.resolve(TEMPLATE_PROPERTIES)) &&
             Files.exists(templateFolder.resolve(TEMPLATE_FOLDER)) &&
             Files.isDirectory(templateFolder.resolve(TEMPLATE_FOLDER));
   }

   /**
    * Parses the properties file a returns a map of parameters and their default values in the
    * properties file,
    * ordered by encounter in the file.
    *
    * @param propertiesFile path to properties file
    * @return map of parameters and their default values
    * @throws IOException      if an exception occurred while reading or parsing the properties
    *                          file
    * @throws BootstrapTemplateException if an error was found when parsing the properties file
    */
   protected LinkedHashMap<String, String> parseTemplateProperties(Path propertiesFile) {
      LinkedHashMap<String, String> properties = new LinkedHashMap<>();

      try {
         for (String line : Files.readAllLines(propertiesFile)) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) {
               continue;
            }
            String[] values = line.split("=", 2);
            if (values.length != 2) {
               throw new BootstrapTemplateException(
                        String.format("Invalid syntax in properties file '%s'", line));
            }
            properties.put(values[0].trim(), values[1].trim());
         }
      } catch (IOException e) {
         throw new BootstrapTemplateException(
                  String.format("Unable to read the properties file '%s'", propertiesFile));
      }
      return properties;
   }
}
