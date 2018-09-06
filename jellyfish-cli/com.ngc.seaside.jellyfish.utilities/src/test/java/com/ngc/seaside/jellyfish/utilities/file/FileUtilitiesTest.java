/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.jellyfish.utilities.file;

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
