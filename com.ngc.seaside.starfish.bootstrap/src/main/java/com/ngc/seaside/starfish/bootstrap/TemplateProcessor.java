package com.ngc.seaside.starfish.bootstrap;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class TemplateProcessor
{
   /**
    * Determines if the unzipped contents of the template are valid.
    *
    * @param templateFolder path to unzipped template folder
    * @throws ExitException if the template has an invalid structure/format
    */
   public static void validateTemplate(Path templateFolder)
   {
      if (!Files.exists(templateFolder.resolve("template.properties"))) {
         throw new ExitException("Invalid template: template.properties not found");
      }
      if (!Files.isRegularFile(templateFolder.resolve("template.properties"))) {
         throw new ExitException("Invalid template: template.properties must be a file");
      }
      if (!Files.exists(templateFolder.resolve("template"))) {
         throw new ExitException("Invalid template: template folder not found");
      }
      if (!Files.isDirectory(templateFolder.resolve("template"))) {
         throw new ExitException("Invalid template: template must be a folder");
      }
   }

   /**
    * Parses the properties file a returns a map of parameters and their default values in the properties file,
    * ordered by encounter in the file.
    *
    * @param propertiesFile path to properties file
    * @return map of parameters and their default values
    * @throws IOException if an exception occurred while reading or parsing the properties file
    * @throws ExitException if an error was found when parsing the properties file
    */
   public static LinkedHashMap<String, String> parseTemplateProperties(Path propertiesFile) throws IOException
   {
      LinkedHashMap<String, String> properties = new LinkedHashMap<>();

      for (String line : Files.readAllLines(propertiesFile)) {
         String trimmed = line.trim();
         if (trimmed.isEmpty() || trimmed.startsWith("#")) {
            continue;
         }
         String[] values = line.split("=", 2);
         if (values.length != 2) {
            throw new ExitException("Invalid syntax in properties file: " + line);
         }
         properties.put(values[0].trim(), values[1].trim());
      }
      return properties;
   }

   /**
    * Unzips the template and returns the path to the folder of the unzipped contents.
    *
    * @implNote the unzipped contents are put into the users temp folder.
    *
    * @param templateZip template zip file
    * @return folder of unzipped contents
    * @throws IOException if an exception occurred while unzipping or storing the files
    */
   public static Path unzip(Path templateZip) throws IOException
   {
      ZipFile zipFile = new ZipFile(templateZip.toString());
      Path unzippedFolderPath = Files.createTempDirectory(null);

      try {
         Enumeration<? extends ZipEntry> entries = zipFile.entries();
         while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            File entryDestination = new File(unzippedFolderPath.toString(), entry.getName());
            if (entry.isDirectory()) {
               entryDestination.mkdirs();
            }
            else {
               entryDestination.getParentFile().mkdirs();
               InputStream in = zipFile.getInputStream(entry);
               OutputStream out = new FileOutputStream(entryDestination);
               IOUtils.copy(in, out);
               IOUtils.closeQuietly(in);
               out.close();
            }
         }
      }
      catch (IOException e) {
         throw new ExitException("An error occurred processing the template zip file: " + e.getMessage());
      }
      finally {
         zipFile.close();
      }

      return unzippedFolderPath;
   }

}
