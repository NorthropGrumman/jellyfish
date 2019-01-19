/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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
                           "publishToMavenLocal",
                           "publish");

      try {
         BuildResult result = runner.build();
         TestingUtilities.assertTaskSuccess(result, null, "build");
         TestingUtilities.assertTaskSuccess(result, null, SystemDescriptorProjectPlugin.ANALYZE_TASK_NAME);
         TestingUtilities.assertTaskSuccess(result, null, "publishToMavenLocal");
         TestingUtilities.assertTaskSuccess(result, null, "publish");
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
