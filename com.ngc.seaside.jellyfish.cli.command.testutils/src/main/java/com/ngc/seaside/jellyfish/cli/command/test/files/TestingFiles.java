package com.ngc.seaside.jellyfish.cli.command.test.files;

import com.google.common.base.Preconditions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestingFiles {

   private TestingFiles() {
   }

   public static void assertFilesEquals(String message, Path expected, Path actual) throws IOException {
      File expectedFile = expected.toFile();
      File actualFile = actual.toFile();
      Preconditions.checkArgument(expectedFile.isFile(), "expected file %s is not a file!", expected);
      assertTrue("expected a file at " + actual,
                 actualFile.isFile());
      assertEquals(message,
                   new String(Files.readAllBytes(expected)),
                   new String(Files.readAllBytes(actual)));
   }
}