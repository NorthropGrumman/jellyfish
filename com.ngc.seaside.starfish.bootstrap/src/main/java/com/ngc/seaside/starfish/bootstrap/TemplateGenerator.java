package com.ngc.seaside.starfish.bootstrap;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for generating an instance of a template
 */
public class TemplateGenerator extends SimpleFileVisitor<Path>
{
   private final VelocityEngine engine = new VelocityEngine();
   private final VelocityContext context = new VelocityContext();
   private final Map<String, String> parametersAndValues;
   private final Path outputFolder;
   private Path inputFolder;
   private final boolean clean;

   /**
    *
    * @param parametersAndValues Map of parameter-values used
    * @param inputFolder folder of the unzipped template
    * @param outputFolder folder for outputting the generated template instance
    * @param clean whether or not to recursively delete already existing folder before creating them again
    */
   public TemplateGenerator(Map<String, String> parametersAndValues, Path inputFolder, Path outputFolder, boolean clean)
   {
      this.outputFolder = outputFolder;
      this.inputFolder = inputFolder;
      this.clean = clean;
      this.parametersAndValues = new HashMap<>(parametersAndValues);
      engine.setProperty("runtime.references.strict", true);
      for (Map.Entry<String, String> entry : parametersAndValues.entrySet()) {
         context.put(entry.getKey(), entry.getValue());
      }
      context.put("Template", TemplateGenerator.class);
      if (clean) {
         try {
            deleteRecursive(outputFolder, true);
         } catch(IOException e) {
            throw new IllegalStateException("Unable to clean");
         }
      }
   }

   /**
    * Converts a object/string with dots (e.g., com.ngc.example) to a string with file separators (e.g., com/ngc/example on Unix).
    * 
    * @implNote this method is used by the Velocity Engine when $Template.asFile($group) is found in order to represent something like a groupId as a file path
    * 
    * @param group object to convert to file path
    * @return a file path of the represented object
    */
   public static String asFile(Object group)
   {
      return group.toString().replace(".", FileSystems.getDefault().getSeparator());
   }

   private Path getOutputPath(Path input) throws IOException
   {
      Path output = outputFolder.resolve(inputFolder.relativize(input)).toAbsolutePath();
      StringWriter w = new StringWriter();
      engine.evaluate(context, w, "", output.toAbsolutePath().toString().replace("\\$", "\\\\$"));
      return Paths.get(w.toString()).toAbsolutePath();
   }

   @Override
   public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes basicFileAttributes) throws IOException
   {
      Path outputFolder = getOutputPath(path);
      try {
         Files.createDirectories(outputFolder);
      }
      catch (FileAlreadyExistsException e) {
      }
      return FileVisitResult.CONTINUE;
   }

   @Override
   public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException
   {
      Path outputFile = getOutputPath(path);
      try (Writer writer = Files.newBufferedWriter(outputFile); Reader reader = Files.newBufferedReader(path)) {
         engine.evaluate(context, writer, "", reader);
      }
      return FileVisitResult.CONTINUE;
   }

   /**
    * Recursively deletes the contents of the given folder.
    * 
    * @param folder folder to delete
    * @param onlySubcontents if true does not delete the folder
    */
   static void deleteRecursive(Path folder, boolean onlySubcontents) throws IOException
   {
      Files.walkFileTree(folder, new SimpleFileVisitor<Path>()
      {

         @Override
         public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
         {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
         }

         @Override
         public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
         {
            if (folder != dir || !onlySubcontents) {
               Files.delete(dir);
            }
            return FileVisitResult.CONTINUE;
         }

      });
   }

}
