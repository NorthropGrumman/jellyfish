package com.ngc.seaside.bootstrap.utilites.file;

import com.google.common.base.Preconditions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 *
 */
public class FileUtilities {

   /**
    * Add lines to the given file.
    *
    * @param fileToAddTo The file in which to add the lines.
    * @param linesToAdd  The lines to add
    * @throws IOException if the given path is unable to be written to.
    */
   public static void addLinesToFile(Path fileToAddTo, List<String> linesToAdd) throws FileUtilitiesException {
      try {
         Preconditions.checkNotNull(fileToAddTo, "The file to write the new lines must not be null");
         Preconditions.checkNotNull(linesToAdd, "The lines to add must not be null");
         Preconditions.checkArgument(fileToAddTo.toFile().isFile(), "The fileToAddTo isn't a file");
         Preconditions.checkArgument(fileToAddTo.toFile().canWrite(), "The file to write isn't writable");
         Preconditions.checkArgument(fileToAddTo.toFile().exists(), "The file in which to write does not exits");
         Preconditions.checkArgument(!linesToAdd.isEmpty(), "The lines must not be empty");

         if (fileToAddTo.toFile().exists()) {
            for (String line : linesToAdd) {
               Files.write(fileToAddTo, String.format("%s%n", line).getBytes(), StandardOpenOption.APPEND);
            }
         }
      } catch (Throwable t) {
         throw new FileUtilitiesException(String.format("Unable to add lines to '%s'", fileToAddTo), t);
      }
   }

}
