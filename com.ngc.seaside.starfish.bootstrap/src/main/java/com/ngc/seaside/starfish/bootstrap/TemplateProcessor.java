package com.ngc.seaside.starfish.bootstrap;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class TemplateProcessor
{
   /**
    * Determines if the unzipped contents of the template are valid. Reports errors and exits the system if any occurred.
    *
    * @param templateFolder path to unzipped template folder
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
    * @throws IOException if an exception occured while reading or parsing the properties file
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
            throw new IllegalArgumentException("Invalid syntax in properties file: " + line);
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

      recursiveDeleteOnShutdownHook(unzippedFolderPath);
      return unzippedFolderPath;
   }

   /**
    * This method will delete a directory and all contents when the JVM shuts down.
    * 
    * @param path the path to the desired directory to be deleted
    */
   public static void recursiveDeleteOnShutdownHook(final Path path)
   {
      Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
      {
         @Override
         public void run()
         {
            try {
               Files.walkFileTree(path, new SimpleFileVisitor<Path>()
               {
                  @Override
                  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
                  {
                     Files.delete(file);
                     return FileVisitResult.CONTINUE;
                  }

                  @Override
                  public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException
                  {
                     if (e == null) {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                     }
                     // directory iteration failed
                     throw e;
                  }
               });
            }
            catch (IOException e) {
               throw new RuntimeException("Failed to delete " + path, e);
            }
         }
      }));
   }
}
