package com.ngc.seaside.jellyfish.cli.command.test.files;

import com.google.common.base.Preconditions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestingFiles {

   private TestingFiles() {
   }

   /**
    * Asserts that two files are equal by comparing bytes
    *
    * @param message the message to display if the assertion fails
    * @param expected the path of the expected output file to read
    * @param actual   the path of the actual output file to read
    */
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

   /**
    * Asserts that two files are equal by comparing the content line by line, excluding trailing empty lines
    *
    * @param message the message to display if the assertion fails
    * @param expected the path of the expected output file to read
    * @param actual   the path of the actual output file to read
    */
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
    * Asserts that the expected contents of a file is equal to actual content, excluding trailing empty lines
    *
    * @param message the message to display if the assertion fails
    * @param expected the path of the expected output file to read
    * @param actual   the actual content to compare
    */
   public static void assertFileLinesEquals(String message, Path expected, List<String> actual) throws IOException {
      File expectedFile = expected.toFile();
      Preconditions.checkArgument(expectedFile.isFile(), "expected file %s is not a file!", expected);

      final List<String> expectedLines = getFileContent(expected);
      final List<String> actualLines = stripTrailingEmptyLines(actual);
      
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

      return stripTrailingEmptyLines(lines);
   }
   
   /**
    * Returns a list of Strings with trailing empty lines removed.
    * 
    * @param lines the input list to trim
    * @return the input list with empty trailing lines removed
    */
   private static List<String> stripTrailingEmptyLines(List<String> lines) {
      final List<String> content = new ArrayList<>();
      content.addAll(lines);
      
      for (int i = content.size()-1; i >= 0; i--){
         if (content.get(i).isEmpty()){
            content.remove(i);
         }            
         else {
            break;
         }
      }   
      
      return content;
   }
}
