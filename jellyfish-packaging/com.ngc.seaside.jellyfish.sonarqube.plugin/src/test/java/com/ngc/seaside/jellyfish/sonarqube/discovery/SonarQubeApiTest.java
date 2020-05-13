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
