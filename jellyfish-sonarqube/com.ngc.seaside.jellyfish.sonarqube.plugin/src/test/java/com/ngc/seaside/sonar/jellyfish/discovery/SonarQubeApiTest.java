package com.ngc.seaside.sonar.jellyfish.discovery;

import org.junit.Test;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
