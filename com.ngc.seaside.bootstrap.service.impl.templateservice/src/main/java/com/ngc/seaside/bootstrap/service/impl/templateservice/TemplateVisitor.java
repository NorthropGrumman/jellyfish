package com.ngc.seaside.bootstrap.service.impl.templateservice;

import com.google.common.base.Preconditions;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Class for generating an instance of a template
 */
public class TemplateVisitor extends SimpleFileVisitor<Path> {
   private final VelocityEngine engine = new VelocityEngine();
   private final VelocityContext context = new VelocityContext();
   private final Path outputFolder;
   private final Path inputFolder;
   private final boolean clean;
   private Path topLevelFolder;
   private TemplateIgnoreComponent templateIgnoreComponent;

   /**
    * Constructor that designates the input and output folders, uses velocity to generate context
    * using the provided parameters, and gives the option to cleanup the output folder in case it
    * was previously generated.
    *
    * @param parametersAndValues     Map of parameter-values used
    * @param inputFolder             folder of the unzipped template
    * @param outputFolder            folder for outputting the generated template instance
    * @param clean                   whether or not to recursively delete already existing folder before creating them
    *                                again
    * @param templateIgnoreComponent used to check files that should be copied instead of evaluated by velocity.
    */
   public TemplateVisitor(Map<String, String> parametersAndValues,
                          Path inputFolder,
                          Path outputFolder,
                          boolean clean,
                          TemplateIgnoreComponent templateIgnoreComponent) {
      this.outputFolder = outputFolder.toAbsolutePath().normalize();
      this.inputFolder = inputFolder.toAbsolutePath().normalize();
      this.clean = clean;
      this.templateIgnoreComponent = templateIgnoreComponent;

      engine.setProperty("runtime.references.strict", true);
      engine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
                         "org.apache.velocity.runtime.log.NullLogSystem");
      for (Map.Entry<String, String> entry : parametersAndValues.entrySet()) {
         context.put(entry.getKey(), entry.getValue());
      }
      context.put("Template", TemplateVisitor.class);
   }

   /**
    * Get the folder that was created for the template. Not the temporary folder.
    *
    * @return the folder.
    */
   public Path getTopLevelFolder() {
      return topLevelFolder;
   }

   /**
    * Converts an object/string with dots (e.g., com.ngc.example) to a string with file separators (e.g.,
    * com/ngc/example on Unix).
    * this method is used by the Velocity Engine when $TemplateVisitor.asPath($groupId, $artifactId)
    * is found in order to represent something like a groupId and artifactId as a file structure (i.e. package).
    *
    * @param path      object to convert to file path
    * @param extraPath any objects that should be added to the path. These will be separated by a . then converted to
    *                  the file system's file separator.
    * @return a file path of the represented object
    */
   public static String asPath(Object path, Object... extraPath) {
      Preconditions.checkNotNull(path, "The path must not be null.");
      Preconditions.checkArgument(!path.toString().trim().isEmpty(), "The initial path can't be empty.");
      StringBuilder builder = new StringBuilder(path.toString());
      if (extraPath != null) {
         for (Object extra : extraPath) {
            Preconditions.checkArgument(!extra.toString().trim().isEmpty(), "The path may not be empty.");
            builder.append(".").append(extra.toString());
         }
      }

      return builder.toString().replace(".", FileSystems.getDefault().getSeparator());
   }

   /**
    * {@inheritDoc}
    *
    * Creates the output folder
    *
    * @param path                the output path
    * @param basicFileAttributes not used
    * @return the results of the file visit
    * @throws IOException if an I/O error occurs
    */
   @Override
   public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
      Path outputFolderTemp = getOutputPath(path);

      if (topLevelFolder == null && !this.outputFolder.toAbsolutePath().equals(outputFolderTemp.toAbsolutePath())) {
         topLevelFolder = outputFolderTemp.normalize();
      }

      if (clean && !path.equals(inputFolder)) {
         try {
            deleteRecursive(outputFolderTemp, true);
         } catch (IOException e) {
            // Ignore cleaning exceptions
         }
      }
      try {
         Files.createDirectories(outputFolderTemp);
      } catch (FileAlreadyExistsException e) {
         // ignored since the file just needs to exist
      }
      return FileVisitResult.CONTINUE;
   }

   /**
    * {@inheritDoc}
    *
    * Replaces the tokens in a given file based on a customized context using velocity engine.
    *
    * @param path                the output path
    * @param basicFileAttributes not used
    * @return the results of the file visit
    * @throws IOException if an I/O error occurs
    */
   @Override
   public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
      Path outputFile = getOutputPath(path);

      if (templateIgnoreComponent.contains(path)) {
         Files.copy(path, outputFile, REPLACE_EXISTING);
      } else {
         try (Writer writer = Files.newBufferedWriter(outputFile);
              Reader reader = Files.newBufferedReader(path)) {
            engine.evaluate(context, writer, "", reader);
         }
      }

      return FileVisitResult.CONTINUE;
   }

   /**
    * Recursively deletes the contents of the given folder.
    *
    * @param folder          folder to delete
    * @param onlySubcontents if true does not delete the folder
    * @throws IOException if an error occurred while deleting
    */
   static void deleteRecursive(Path folder, boolean onlySubcontents) throws IOException {
      Files.walkFileTree(folder, new SimpleFileVisitor<Path>() {
         @Override
         public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
         }

         @Override
         public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            if (folder != dir || !onlySubcontents) {
               Files.delete(dir);
            }
            return FileVisitResult.CONTINUE;
         }
      });
   }

   /**
    * Converts any velocity-like formatting in the input's filename and returns the output locations of the input file.
    *
    * @param input path to input file or folder
    * @return output path of file or folder
    */
   private Path getOutputPath(Path input) {
      if (templateIgnoreComponent.contains(input)) {
         String path = templateIgnoreComponent.getKey(input);
         Matcher m = Pattern.compile("\\[([^)]+)]").matcher(path);
         while(m.find()) {
            String value = String.format("[%s]",  m.group(1));
            StringWriter stringWriter = new StringWriter();

            try {
               engine.evaluate(context, stringWriter, "evaluate output", m.group(1));
               path = path.replace(value, stringWriter.toString());
            } catch (IOException e) {
               //ignored since StringWriter won't throw an IOException
            }
         }

         return Paths.get(outputFolder.toFile().getAbsolutePath(), path).toAbsolutePath();
      }

      Path output = outputFolder.resolve(inputFolder.relativize(input)).toAbsolutePath();
      StringWriter stringWriter = new StringWriter();
      String outputPath = output.toAbsolutePath().toString().replace("\\$", "\\\\$");
      try {
         engine.evaluate(context, stringWriter, "evaluate output", outputPath);
      } catch (IOException e) {
         // ignored since StringWriter won't throw an IOException
      }

      return Paths.get(stringWriter.toString()).toAbsolutePath();
   }

}
