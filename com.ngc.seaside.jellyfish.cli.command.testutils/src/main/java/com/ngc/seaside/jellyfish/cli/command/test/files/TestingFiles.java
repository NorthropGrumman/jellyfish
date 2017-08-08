package com.ngc.seaside.jellyfish.cli.command.test.files;

import com.google.common.base.Preconditions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestingFiles {

   private TestingFiles() {
   }

   public static void assertFileBytesEquals(String message, Path expected, Path actual) throws IOException {
      File expectedFile = expected.toFile();
      File actualFile = actual.toFile();
      Preconditions.checkArgument(expectedFile.isFile(), "expected file %s is not a file!", expected);
      assertTrue("expected a file at " + actual,
                 actualFile.isFile());

      assertEquals(message,
                   new String(Files.readAllBytes(expected)),
                   new String(Files.readAllBytes(actual)));
   }

   public static void assertFileLinesEquals(String message, Path expected, Path actual) throws IOException {
      File expectedFile = expected.toFile();
      File actualFile = actual.toFile();
      Preconditions.checkArgument(expectedFile.isFile(), "expected file %s is not a file!", expected);
      assertTrue("expected a file at " + actual,
                 actualFile.isFile());

      final List<String> expectedLines = getFileContent(expected);
      final List<String> actualLines = getFileContent(actual);
      
      assertEquals(message + " [number of lines of content]", expectedLines.size(), actualLines.size());
      
      for (int i = 0; i < expectedLines.size(); i++){
          assertEquals(message + " [line number " + (i+1) + " does not match]", expectedLines.get(i), actualLines.get(i));
      }
   }

   /**
    * Gets the content of a file and strips any trailing empty lines
    *
    * @param path the path of the file to read
    * @return the content of the file as a list of Strings
    */
   private static List<String> getFileContent(Path filepath) throws IOException {
      final List<String> lines = Files.readAllLines(filepath);

      // trim trailing empty lines
      for (int i = lines.size()-1; i >= 0; i--){
         if (lines.get(i).isEmpty()){
            lines.remove(i);
         }            
         else {
            break;
         }
      }
      
      return lines;
   }
}
