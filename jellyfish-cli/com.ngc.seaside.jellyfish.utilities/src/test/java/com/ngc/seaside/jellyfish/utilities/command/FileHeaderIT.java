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
