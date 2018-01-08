package com.ngc.seaside.bootstrap.utilities.file;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

/**
 *
 */
public class FileUtilitiesTest {

   @Rule
   public TemporaryFolder testFolder = new TemporaryFolder();


   @Test
   public void doesAddLineToFile() throws IOException, FileUtilitiesException {
      File settingsFile = testFolder.newFile("settings.gradle");
      Path settingsPath = Paths.get(settingsFile.getAbsolutePath());

      List<String> lines = new ArrayList<>();
      lines.add("line1");
      lines.add("line2");

      FileUtilities.addLinesToFile(settingsPath, lines);

      List<String> newLines = Files.readAllLines(settingsPath);

      assertTrue(newLines.contains("line1"));
      assertTrue(newLines.contains("line2"));
   }

}
