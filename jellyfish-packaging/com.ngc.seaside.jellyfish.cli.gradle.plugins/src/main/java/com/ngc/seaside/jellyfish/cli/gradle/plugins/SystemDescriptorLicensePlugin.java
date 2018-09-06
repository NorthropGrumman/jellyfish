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
package com.ngc.seaside.jellyfish.cli.gradle.plugins;

import com.ngc.seaside.gradle.api.AbstractProjectPlugin;
import com.ngc.seaside.gradle.plugins.parent.SeasideRootParentPlugin;

import nl.javadude.gradle.plugins.license.License;
import nl.javadude.gradle.plugins.license.LicenseExtension;
import nl.javadude.gradle.plugins.license.LicensePlugin;

import org.gradle.api.Project;

import java.util.Arrays;

/**
 * A plugin that can be applied to System Descriptor projects to configure a common license or header for all files in
 * the project.  Use the {@code license} extension to configure the file that contains the contents of the license.
 * For example,
 *
 * <pre>{@code
 *   apply plugin: 'com.ngc.seaside.jellyfish.system-descriptor'
 *   apply plugin: 'com.ngc.seaside.jellyfish.system-descriptor-license'
 *
 *   license {
 *      file('../LICENSE')
 *   }
 * }</pre>
 *
 * Use the tasks {@code license} to check for license consistency and {@code licenseFormat} to apply the license to all
 * files.
 */
public class SystemDescriptorLicensePlugin extends AbstractProjectPlugin {

   /**
    * The task that checks the license on source files in the main source set.
    */
   public static final String LICENSE_MAIN_TASK_NAME = "licenseMain";

   /**
    * The task that checks the license on source files in the test source set.
    */
   public static final String LICENSE_TEST_TASK_NAME = "licenseTest";

   /**
    * The task that applies the license to the source files in the main source set.
    */
   public static final String FORMAT_MAIN_TASK_BASE_NAME = "licenseFormatMain";

   /**
    * The task that applies the license to the source files in the test source set.
    */
   public static final String FORMAT_TEST_TASK_BASE_NAME = "licenseFormatTest";

   @Override
   protected void doApply(Project project) {
      applyPlugins(project);
      configureLicense(project);
      configureTasks(project);
   }

   private void applyPlugins(Project project) {
      project.getPlugins().apply(LicensePlugin.class);
      // Reuse the license logic from the seaside root parent plugin.
      project.getPlugins().apply(SeasideRootParentPlugin.class);
   }


   private void configureLicense(Project project) {
      LicenseExtension license = project.getExtensions().getByType(LicenseExtension.class);
      license.mapping("gradle", "SLASHSTAR_STYLE");
      license.mapping("sd", "JAVADOC_STYLE");
      license.mapping("feature", "SCRIPT_STYLE");
   }

   private void configureTasks(Project project) {
      for (String taskName : Arrays.asList(LICENSE_MAIN_TASK_NAME,
                                           FORMAT_MAIN_TASK_BASE_NAME)) {
         License licenseTask = (License) project.getTasks().getByName(taskName);
         licenseTask.setSource("src/main");
         licenseTask.include("**/*.sd");
         licenseTask.include("**/*.properties");
      }

      for (String taskName : Arrays.asList(LICENSE_TEST_TASK_NAME,
                                           FORMAT_TEST_TASK_BASE_NAME)) {
         License licenseTask = (License) project.getTasks().getByName(taskName);
         licenseTask.setSource("src/test");
         licenseTask.include("**/*.feature");
      }
   }
}
