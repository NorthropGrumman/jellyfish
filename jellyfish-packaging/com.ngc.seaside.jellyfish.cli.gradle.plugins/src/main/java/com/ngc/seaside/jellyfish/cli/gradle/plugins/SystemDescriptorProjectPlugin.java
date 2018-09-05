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
import com.ngc.seaside.gradle.plugins.release.SeasideReleaseRootProjectPlugin;
import com.ngc.seaside.gradle.plugins.repository.SeasideRepositoryPlugin;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.cli.gradle.tasks.JellyFishCliCommandTask;
import com.ngc.seaside.jellyfish.sonarqube.properties.SystemDescriptorProperties;

import groovy.util.Node;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.XmlProvider;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.ProjectDependency;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.plugins.BasePlugin;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.plugins.MavenPlugin;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin;
import org.gradle.api.publish.maven.tasks.GenerateMavenPom;
import org.gradle.api.publish.plugins.PublishingPlugin;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.jvm.tasks.Jar;
import org.gradle.language.base.plugins.LifecycleBasePlugin;
import org.sonarqube.gradle.SonarQubeExtension;
import org.sonarqube.gradle.SonarQubePlugin;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * A plugin that can be applied to a System Descriptor project. When a build is executed, the System Descriptor project
 * will be validated with JellyFish and then a ZIP of the project will be created. The ZIP can then be installed to
 * a local Maven repository or uploaded to a remote Maven repository.
 *
 * <p>
 * This plugin provides the {@value #SD_CONFIGURATION_NAME} configuration so that system descriptor projects can depend
 * on other system descriptor project.
 * Example:
 * <pre>
 * apply plugin: 'com.ngc.seaside.jellyfish.system-descriptor'
 * dependencies {
 *    sd project(':')
 *    sd "com.ngc.seaside.jellyfish.examples:threatevaluation.descriptor:$threatEvalVersion"
 * }
 * </pre>
 *
 * <p>
 * This plugin adds the {@link SystemDescriptorAnalysisExtension#EXTENSION_NAME sdAnalysis} extension of type
 * {@link SystemDescriptorAnalysisExtension}. When the {@link SonarQubePlugin sonarqube} plugin is applied, this
 * extension can be used to configure which analysis commands should be run by the sonarqube analysis.
 * Example:
 *
 * <pre>
 * apply plugin: 'com.ngc.seaside.jellyfish.system-descriptor'
 * apply plugin: 'org.sonarqube'
 *
 * sdAnalysis {
 *    commands 'inputs-outputs-analysis', 'foo-analysis'
 *    arg 'model', 'com.ngc.SomeModel'
 *    arg 'verbose', true
 *    args [deploymentModel : 'com.ngc.SomeDeploymentModel', what : 'ever']
 * }
 * </pre>
 */
public class SystemDescriptorProjectPlugin extends AbstractProjectPlugin {

   public static final String SD_CONFIGURATION_NAME = "sd";
   public static final String SD_JAR_TASK_NAME = "sdJar";
   public static final String TEST_JAR_TASK_NAME = "testJar";
   public static final String VALIDATE_TASK_NAME = "validateSd";

   @Override
   protected void doApply(Project project) {
      applyPlugins(project);
      createConfigurations(project);
      configureTasks(project);
      configurePublishing(project);
      configureExtensions(project);
   }

   private void createConfigurations(Project project) {
      ConfigurationContainer configurations = project.getConfigurations();
      Configuration sd = configurations.create(SD_CONFIGURATION_NAME);
      sd.getResolutionStrategy().failOnVersionConflict();
      project.afterEvaluate(this::addSdDependenciesToBuiltInConfigurations);
   }

   private void configureTasks(Project project) {
      TaskContainer tasks = project.getTasks();

      Path sd = project.getProjectDir().toPath().resolve(Paths.get("src", "main", "sd"));
      Path sdResources = project.getProjectDir().toPath().resolve(Paths.get("src", "main", "resources"));
      Path gherkin = project.getProjectDir().toPath().resolve(Paths.get("src", "test", "gherkin"));
      Path gherkinResources = project.getProjectDir().toPath().resolve(Paths.get("src", "test", "resources"));

      JavaPluginConvention convention = project.getConvention().getPlugin(JavaPluginConvention.class);
      SourceSetContainer container = convention.getSourceSets();
      SourceSet main = container.getByName(SourceSet.MAIN_SOURCE_SET_NAME);
      SourceSet test = container.getByName(SourceSet.TEST_SOURCE_SET_NAME);
      main.getResources().setSrcDirs(Arrays.asList(sd, sdResources));
      test.getResources().setSrcDirs(Arrays.asList(gherkin, gherkinResources));

      Task build = tasks.getByName(LifecycleBasePlugin.BUILD_TASK_NAME);

      Task sdJar = tasks.create(SD_JAR_TASK_NAME, Jar.class, task -> {
         task.setExtension("zip");
         task.from(main.getOutput());
         build.dependsOn(task);
      });

      Task testJar = tasks.create(TEST_JAR_TASK_NAME, Jar.class, task -> {
         task.setClassifier("tests");
         task.setExtension("zip");
         task.from(test.getOutput());
         build.dependsOn(task);
      });

      tasks.create(VALIDATE_TASK_NAME, JellyFishCliCommandTask.class, task -> {
         task.setCommand("validate");
         task.setDescription("Validates the system descriptor project");
         task.setGroup(LifecycleBasePlugin.BUILD_GROUP);
         task.setArguments(Collections.singletonMap(CommonParameters.INPUT_DIRECTORY.getName(),
                                                    project.getProjectDir().toString()));
         build.dependsOn(task);
         sdJar.dependsOn(task);
         testJar.dependsOn(task);
         task.dependsOn(tasks.withType(GenerateMavenPom.class));

         // Dependent local projects must be built and installed first
         project.getConfigurations().getByName(SD_CONFIGURATION_NAME).getAllDependencies()
               .withType(ProjectDependency.class).all(dependency -> dependency.getDependencyProject()
                     .getTasks()
                     .matching(projectTask -> MavenPlugin.INSTALL_TASK_NAME.equals(projectTask.getName()))
                     .all(task::dependsOn));
      });
   }

   private void applyPlugins(Project project) {
      // Java plugin is required to install a model project locally.
      project.getPlugins().apply(JavaPlugin.class);

      // Disable unnecessary parts of the java plugin
      project.getTasks().getByName(JavaPlugin.JAR_TASK_NAME).setEnabled(false);
      project.getConfigurations().all(config -> config.getOutgoing().getArtifacts()
            .removeIf(artifact -> "jar".equals(artifact.getExtension())));
      project.getConfigurations().getByName(JavaPlugin.TEST_COMPILE_CONFIGURATION_NAME)
            .setExtendsFrom(Collections.emptySet());

      project.getPlugins().apply(MavenPublishPlugin.class);
      // Alias previously-used tasks from maven plugin
      project.getTasks().create(MavenPlugin.INSTALL_TASK_NAME,
                                task -> task.dependsOn(MavenPublishPlugin.PUBLISH_LOCAL_LIFECYCLE_TASK_NAME));
      project.getTasks().create(BasePlugin.UPLOAD_ARCHIVES_TASK_NAME,
                                task -> task.dependsOn(PublishingPlugin.PUBLISH_LIFECYCLE_TASK_NAME));
      project.getTasks().create("upload", task -> task.dependsOn(BasePlugin.UPLOAD_ARCHIVES_TASK_NAME));

      project.getPlugins().apply(SeasideReleaseRootProjectPlugin.class);
      configureReleaseTask(project);

      project.getPlugins().apply(SeasideRepositoryPlugin.class);
   }

   private void configureReleaseTask(Project project) {
      TaskContainer tasks = project.getTasks();
      Task createTag = tasks.getByName(SeasideReleaseRootProjectPlugin.RELEASE_CREATE_TAG_TASK_NAME);
      Task bumpVersion = tasks.getByName(SeasideReleaseRootProjectPlugin.RELEASE_BUMP_VERSION_TASK_NAME);
      Task push = tasks.getByName(SeasideReleaseRootProjectPlugin.RELEASE_PUSH_TASK_NAME);
      Collection<Task> uploadTasks = Arrays.asList(
            tasks.getByName(MavenPublishPlugin.PUBLISH_LOCAL_LIFECYCLE_TASK_NAME),
            tasks.getByName(BasePlugin.UPLOAD_ARCHIVES_TASK_NAME),
            tasks.getByName("upload"));

      project.getTasks().create("release", task -> {
         task.setGroup(SeasideReleaseRootProjectPlugin.RELEASE_ROOT_PROJECT_TASK_GROUP_NAME);
         task.setDescription("Releases this project.");
         task.dependsOn(createTag, uploadTasks, bumpVersion, push);
      });
      project.getTasks().getByName(PublishingPlugin.PUBLISH_LIFECYCLE_TASK_NAME)
            .mustRunAfter(SeasideReleaseRootProjectPlugin.RELEASE_CREATE_TAG_TASK_NAME);
      project.getTasks().getByName(SeasideReleaseRootProjectPlugin.RELEASE_BUMP_VERSION_TASK_NAME)
            .mustRunAfter(uploadTasks);

      uploadTasks.forEach(task -> task.mustRunAfter(createTag));
      bumpVersion.mustRunAfter(uploadTasks);
      push.mustRunAfter(bumpVersion);
   }

   private void configurePublishing(Project project) {
      project.getExtensions().configure(PublishingExtension.class, publishing -> {
         publishing.publications(publications -> {
            publications.create("mavenSd", MavenPublication.class, publication -> {
               Jar sdJar = (Jar) project.getTasks().getByName(SD_JAR_TASK_NAME);
               Jar testJar = (Jar) project.getTasks().getByName(TEST_JAR_TASK_NAME);
               publication.setGroupId(project.getGroup().toString());
               publication.artifact(sdJar);
               publication.artifact(testJar);
               publication.getPom().withXml(xml -> configurePom(project, xml));
            });
         });
      });
   }

   private void configurePom(Project project, XmlProvider xml) {
      Configuration sd = project.getConfigurations().getByName(SD_CONFIGURATION_NAME);
      Jar sdJar = (Jar) project.getTasks().getByName(SD_JAR_TASK_NAME);
      Jar testJar = (Jar) project.getTasks().getByName(TEST_JAR_TASK_NAME);
      Node dependenciesNode = xml.asNode().appendNode("dependencies");
      sd.getDependencies().forEach(dependency -> {
         Node sdNode = dependenciesNode.appendNode("dependency");
         sdNode.appendNode("groupId", dependency.getGroup());
         sdNode.appendNode("artifactId", dependency.getName());
         sdNode.appendNode("version", dependency.getVersion());
         sdNode.appendNode("type", sdJar.getExtension());
         sdNode.appendNode("scope", "compile");
         Node featureNode = dependenciesNode.appendNode("dependency");
         featureNode.appendNode("groupId", dependency.getGroup());
         featureNode.appendNode("artifactId", dependency.getName());
         featureNode.appendNode("version", dependency.getVersion());
         featureNode.appendNode("classifier", testJar.getClassifier());
         featureNode.appendNode("type", testJar.getExtension());
         featureNode.appendNode("scope", "test");
      });
   }

   private void configureExtensions(Project project) {
      ExtensionContainer extensions = project.getExtensions();
      extensions.create(SystemDescriptorLanguageExtension.EXTENSION_NAME, SystemDescriptorLanguageExtension.class);
      SystemDescriptorAnalysisExtension analysisExtension = extensions.create(
            SystemDescriptorAnalysisExtension.EXTENSION_NAME, SystemDescriptorAnalysisExtension.class);

      project.getPlugins().withType(SonarQubePlugin.class, plugin -> {
         SonarQubeExtension sonarqubeExtension =
               (SonarQubeExtension) extensions.getByName(SonarQubeExtension.SONARQUBE_EXTENSION_NAME);
         // This callback will only be executed when Sonarqube properties should be set.  This will probably happen
         // after the project is evaluated.
         sonarqubeExtension.properties(properties -> {
            if (!analysisExtension.getCommands().isEmpty()) {
               // Setup the Sonarqube properties to identify which analysis to run with the Sonarqube plugin is
               // executed.
               String commands = analysisExtension.getCommands().stream().collect(Collectors.joining(","));
               String args = analysisExtension.getArgs().entrySet().stream()
                     .map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.joining(","));

               properties.property("sonar.sources", project.file("src/main/sd"));
               properties.property(SystemDescriptorProperties.JELLYFISH_ANALYSIS_KEY, commands);
               properties.property(SystemDescriptorProperties.JELLYFISH_CLI_EXTRA_ARGUMENTS_KEY, args);
            }
         });
         project.getTasks().getByName(SonarQubeExtension.SONARQUBE_TASK_NAME).dependsOn(VALIDATE_TASK_NAME);
      });
   }

   @SuppressWarnings("deprecation")
   private void addSdDependenciesToBuiltInConfigurations(Project project) {
      SystemDescriptorLanguageExtension languageExtension =
            project.getExtensions().getByType(SystemDescriptorLanguageExtension.class);
      Configuration sd = project.getConfigurations().getByName(SD_CONFIGURATION_NAME);

      DependencyHandler dependencies = project.getDependencies();
      // Include the default dependencies for the language if configured.
      languageExtension.getDefaultLanguageDependencies().forEach(gav -> dependencies.add(sd.getName(), gav));

      // Copy the dependencies from the SD configuration to the configurations used by Java.  We do this so we can
      // easily include the correct POM with uploading SD projects to Nexus with the Maven deployer/uploader plugin.
      sd.getDependencies().forEach(dependency -> {
         String compile = String.format("%s:%s:%s",
                                        dependency.getGroup(), dependency.getName(), dependency.getVersion());
         String testCompile = String.format("%s:%s:%s:tests@zip",
                                            dependency.getGroup(), dependency.getName(), dependency.getVersion());
         dependencies.add(JavaPlugin.COMPILE_CONFIGURATION_NAME, compile);
         dependencies.add(JavaPlugin.TEST_COMPILE_CONFIGURATION_NAME, testCompile);
      });
   }
}
