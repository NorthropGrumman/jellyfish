package com.ngc.seaside.jellyfish.cli.gradle.plugins;

import com.ngc.seaside.gradle.api.AbstractProjectPlugin;
import com.ngc.seaside.gradle.plugins.repository.SeasideRepositoryPlugin;
import com.ngc.seaside.jellyfish.cli.gradle.DerivedRootProjectExtension;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.plugins.BasePlugin;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.language.base.plugins.LifecycleBasePlugin;

import java.io.File;

/**
 * A plugin for assisting with jellyfish-generated service project. This plugin should be applied to the
 * {@link Project#getRootProject() root} project. This plugin provides the {@value #EXTENSION_NAME} extension of type
 * {@link DerivedRootProjectExtension}. The extension's {@link DerivedRootProjectExtension#setModel(String) model},
 * {@link DerivedRootProjectExtension#setProject(Object) project}, and (optionally)
 * {@link DerivedRootProjectExtension#setDeploymentModel(String) deploymentModel} should be set. Example:
 * 
 * <pre>
 * apply plugin: 'com.ngc.seaside.jellyfish.system-descriptor-derived-root'
 * systemDescriptor {
 *    model = 'com.ngc.seaside.threateval.TrackPriorityService'
 *    deploymentModel = 'com.ngc.seaside.threateval.deployment.DemoThreatEvalDeployment'
 *    project = 'com.ngc.seaside.threateval:threatevaluation.descriptor:2.7.0'
 * }
 * </pre>
 * 
 * This plugin adds the task {@value #CLEAN_GEN_TASK_NAME} for completely cleaning generated projects.
 */
public class SystemDescriptorDerivedRootProjectPlugin extends AbstractProjectPlugin {

   public static final String EXTENSION_NAME = "systemDescriptor";
   public static final String SD_CONFIGURATION_NAME = "sd";
   public static final String GHERKIN_CONFIGURATION_NAME = "gherkin";
   public static final String GENERATE_SD_TASK_NAME = "generateSd";
   public static final String GENERATE_FEATURES_TASK_NAME = "generateFeatures";
   public static final String CLEAN_GEN_TASK_NAME = "clean-gen";

   @Override
   protected void doApply(Project project) {
      applyPlugins(project);
      createExtension(project);
      createConfigurations(project);
      createTasks(project);
   }

   private void applyPlugins(Project project) {
      project.getPlugins().apply(BasePlugin.class);
      project.getPlugins().apply(SeasideRepositoryPlugin.class);
   }

   private void createExtension(Project project) {
      project.getExtensions().create(EXTENSION_NAME, DerivedRootProjectExtension.class, project);
   }

   private void createConfigurations(Project project) {
      ConfigurationContainer configurations = project.getConfigurations();
      configurations.create(SD_CONFIGURATION_NAME, config -> {
         config.getResolutionStrategy().failOnVersionConflict();
      });
      configurations.create(GHERKIN_CONFIGURATION_NAME, config -> {
         config.getResolutionStrategy().failOnVersionConflict();
         config.setTransitive(false);
      });
   }

   private void createTasks(Project project) {
      TaskContainer tasks = project.getTasks();
      DerivedRootProjectExtension systemDescriptor =
               (DerivedRootProjectExtension) project.getExtensions().getByName(EXTENSION_NAME);
      tasks.create(GENERATE_SD_TASK_NAME, Copy.class, task -> {
         Configuration sd = project.getConfigurations().getByName(SD_CONFIGURATION_NAME);
         project.afterEvaluate(__ -> {
            project.getDependencies().add(SD_CONFIGURATION_NAME, systemDescriptor.getProject());
            for (File file : sd) {
               task.from(project.zipTree(file));
            }
            task.into(systemDescriptor.getDirectory());
         });
         tasks.getByName(LifecycleBasePlugin.BUILD_TASK_NAME).dependsOn(task);
      });
      tasks.create(GENERATE_FEATURES_TASK_NAME, Copy.class, task -> {
         Configuration gherkin = project.getConfigurations().getByName(GHERKIN_CONFIGURATION_NAME);
         project.afterEvaluate(__ -> {
            Dependency projectDependency = systemDescriptor.getProject();
            String testArtifact = projectDependency.getGroup() + ":" + projectDependency.getName() + ":"
                     + projectDependency.getVersion() + ":tests@zip";
            project.getDependencies().add(GHERKIN_CONFIGURATION_NAME, testArtifact);
            for (File file : gherkin) {
               task.from(project.zipTree(file));
            }
            task.into(systemDescriptor.getTestDirectory());
         });
      });
      tasks.create(SystemDescriptorDerivedRootProjectPlugin.CLEAN_GEN_TASK_NAME, task -> {
         task.setGroup(BasePlugin.BUILD_GROUP);
         task.setDescription("Deletes generated jellyfish projects");
      });
   }
}
