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
package com.ngc.seaside.jellyfish.sonarqube.discovery;

import org.junit.Test;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class SonarQubeApiTest {

   @Test
   public void howDoWeFilterFilesWithFileSystemPredicates() {
      File baseDir = new File("target/test-classes");
      DefaultFileSystem fs = new DefaultFileSystem(baseDir).add(addInputFile(baseDir));
      FilePredicates p = fs.predicates();
      assertTrue(
            "no files available",
            fs.hasFiles(p.all()) && fs.inputFiles(p.hasFilename("Model.sd")).iterator().hasNext()
      );
   }

   private DefaultInputFile addInputFile(File baseDir) {
      return
            new TestInputFileBuilder("somekey", "Model.sd")
                  .setModuleBaseDir(baseDir.toPath())
                  .setLanguage("systemdescriptor")
                  .initMetadata("package my.model\nmodel Model {\n}")
                  .build();
   }
}
