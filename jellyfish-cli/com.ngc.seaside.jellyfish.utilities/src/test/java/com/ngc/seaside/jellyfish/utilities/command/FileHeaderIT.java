package com.ngc.seaside.jellyfish.utilities.command;

import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class FileHeaderIT {

   private FileHeader fileHeader;

   @Test
   public void testDoesGetConfiguredHeader() throws Throwable {
      fileHeader = new FileHeader(Paths.get(FileHeaderIT.class
                                                  .getClassLoader()
                                                  .getResource("exampleHeader.txt")
                                                  .toURI()));

      assertHeader(fileHeader.getJava(),
                   "/**",
                   " * This is a",
                   " * test of",
                   " * an example",
                   " * header.",
                   " */");
      assertHeader(fileHeader.getGradle(),
                   "/*",
                   " * This is a",
                   " * test of",
                   " * an example",
                   " * header.",
                   " */");
      assertHeader(fileHeader.getProperties(),
                   "#",
                   "# This is a",
                   "# test of",
                   "# an example",
                   "# header.",
                   "#");
   }

   @Test
   public void testDoesGetDefaultHeader() {
      fileHeader = FileHeader.DEFAULT_HEADER;
      assertHeader(fileHeader.getJava(),
                   "/**",
                   " * This is the default header.",
                   " */");
      assertHeader(fileHeader.getGradle(),
                   "/*",
                   " * This is the default header.",
                   " */");
      assertHeader(fileHeader.getProperties(),
                   "#",
                   "# This is the default header.",
                   "#");
   }

   @Test
   public void testDoesMakeEmptyHeaderIfHeaderFileIsEmpty() throws Throwable {
      fileHeader = new FileHeader(Files.createTempFile("FileHeaderIT", "test"));

      assertTrue("header should be empty if file is empty!",
                 fileHeader.getJava().isEmpty());
      assertTrue("header should be empty if file is empty!",
                 fileHeader.getGradle().isEmpty());
      assertTrue("header should be empty if file is empty!",
                 fileHeader.getProperties().isEmpty());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testDoesHandleMissingLicenseFile() {
      fileHeader = new FileHeader(Paths.get("does", "not", "exists"));
   }

   private static void assertHeader(String actual, String... expected) {
      assertArrayEquals("header not correct!",
                        expected,
                        actual.split(System.lineSeparator()));
   }
}
