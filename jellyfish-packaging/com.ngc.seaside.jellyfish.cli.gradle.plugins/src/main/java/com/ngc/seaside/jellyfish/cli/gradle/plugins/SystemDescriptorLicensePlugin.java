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
 * <p>
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
