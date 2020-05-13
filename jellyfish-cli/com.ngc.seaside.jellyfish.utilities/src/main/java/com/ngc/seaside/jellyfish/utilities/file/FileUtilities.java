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
package com.ngc.seaside.jellyfish.utilities.file;

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

   private FileUtilities() {

   }

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
