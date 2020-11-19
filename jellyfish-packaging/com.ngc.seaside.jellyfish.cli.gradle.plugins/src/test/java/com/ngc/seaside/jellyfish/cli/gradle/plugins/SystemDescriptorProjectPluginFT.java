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
package com.ngc.seaside.jellyfish.cli.gradle.plugins;

import com.ngc.seaside.gradle.util.test.SeasideGradleRunner;
import com.ngc.seaside.gradle.util.test.TestingUtilities;

import org.apache.commons.io.FileUtils;
import org.gradle.testfixtures.ProjectBuilder;
import org.gradle.testkit.runner.BuildResult;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.fail;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeNotNull;

public class SystemDescriptorProjectPluginFT {

   private File projectDir;

   @Before
   public void before() throws IOException {
      File source = new File("src/test/resources/sd/com.ngc.example.sd");
      projectDir = new File("build/functionalTest/sd/com.ngc.example.sd");
      Files.createDirectories(projectDir.toPath());
      FileUtils.copyDirectory(source, projectDir);
      ProjectBuilder.builder().withProjectDir(projectDir).build();
   }

   @Test
   public void runsGradleBuildWithSuccess() {
      assumePropertyExists("nexusSnapshots");
      assumePropertyExists("nexusUsername");
      assumePropertyExists("nexusPassword");

      SeasideGradleRunner runner = SeasideGradleRunner.create()
            .withNexusProperties()
            .withProjectDir(projectDir)
            .withPluginClasspath()
            .forwardOutput()
            .withArguments("clean",
                           "build",
                           SystemDescriptorProjectPlugin.ANALYZE_TASK_NAME,
                           "publishToMavenLocal"
                           /*"publish"*/);

      try {
         BuildResult result = runner.build();
         TestingUtilities.assertTaskSuccess(result, null, "build");
         TestingUtilities.assertTaskSuccess(result, null, SystemDescriptorProjectPlugin.ANALYZE_TASK_NAME);
         TestingUtilities.assertTaskSuccess(result, null, "publishToMavenLocal");
         //TestingUtilities.assertTaskSuccess(result, null, "publish");
      } catch (Exception e) {
         e.printStackTrace(new PasswordHidingWriter(System.err));
         fail();
      }
   }

   private void assumePropertyExists(String key) {
      String property = System.getProperty(key);
      assumeNotNull(property);
      assumeFalse(property.isEmpty());
      assumeFalse("null".equals(property));
   }

}
