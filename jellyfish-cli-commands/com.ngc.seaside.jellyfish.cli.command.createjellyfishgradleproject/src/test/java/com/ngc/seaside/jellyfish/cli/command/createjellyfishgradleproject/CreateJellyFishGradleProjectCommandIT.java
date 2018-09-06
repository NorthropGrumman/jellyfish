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
package com.ngc.seaside.jellyfish.cli.command.createjellyfishgradleproject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedTemplateService;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.DependencyScope;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateJellyFishGradleProjectCommandIT {

   private CreateJellyFishGradleProjectCommand command;

   @Mock
   private IBuildManagementService buildManagementService;

   @Mock
   private ILogService logService;

   @Mock(answer = Answers.RETURNS_DEEP_STUBS)
   private IJellyFishCommandOptions options;

   private ITemplateService templateService;

   @Rule
   public TemporaryFolder temporaryDirectory = new TemporaryFolder();

   private File outputDirectory;

   @Before
   public void setup() throws IOException {
      outputDirectory = temporaryDirectory.newFolder();

      templateService = new MockedTemplateService()
            .useRealPropertyService()
            .useDefaultUserValues(true)
            .setTemplateDirectory(CreateJellyFishGradleProjectCommandIT.class.getPackage().getName(),
                                  Paths.get("src/main/template"));

      IBuildDependency dependency1 = newDependency("com.google.protobuf",
                                                   "protobuf-gradle-plugin",
                                                   "1.2.3",
                                                   "protobufVersion");
      IBuildDependency dependency2 = newDependency("com.google.guava",
                                                   "guava",
                                                   "1.2.3",
                                                   "guavaVersion");
      IBuildDependency dependency3 = newDependency("com.test",
                                                   "test-stuff",
                                                   "1.4.5",
                                                   "testVersion");
      when(buildManagementService.getRegisteredDependencies(options, DependencyScope.BUILDSCRIPT))
            .thenReturn(Collections.singleton(dependency1));
      when(buildManagementService.getRegisteredDependencies(options, DependencyScope.BUILD))
            .thenReturn(Collections.singleton(dependency2));
      when(buildManagementService.getRegisteredDependencies(options, DependencyScope.TEST))
            .thenReturn(Collections.singleton(dependency3));

      IProjectInformation project = newProject("project1", "p1");
      when(buildManagementService.getRegisteredProjects())
            .thenReturn(Collections.singleton(project));

      command = new CreateJellyFishGradleProjectCommand();
      command.setLogService(logService);
      command.setTemplateService(templateService);
      command.setBuildManagementService(buildManagementService);
      command.activate();
   }

   @Test
   public void testCommand() throws IOException {
      final String projectName = "test-project-1";
      final String version = "1.0";
      final String sdgav = "com.ngc.seaside.system1:system.descriptor:1.0-SNAPSHOT";
      final String model = "com.ngc.seaside.Model1";

      DefaultParameterCollection collection = new DefaultParameterCollection();
      collection.addParameter(new DefaultParameter<>(CreateJellyFishGradleProjectCommand.PROJECT_NAME_PROPERTY,
                                                     projectName));
      collection.addParameter(new DefaultParameter<>(CreateJellyFishGradleProjectCommand.VERSION_PROPERTY,
                                                     version));
      collection.addParameter(new DefaultParameter<>(CreateJellyFishGradleProjectCommand.SYSTEM_DESCRIPTOR_GAV_PROPERTY,
                                                     sdgav));
      collection.addParameter(new DefaultParameter<>(CreateJellyFishGradleProjectCommand.MODEL_NAME_PROPERTY,
                                                     model));
      collection.addParameter(new DefaultParameter<>(CreateJellyFishGradleProjectCommand.OUTPUT_DIR_PROPERTY,
                                                     outputDirectory.toString()));
      when(options.getParameters()).thenReturn(collection);
      IModel mockedModel = mock(IModel.class);
      when(options.getSystemDescriptor().findModel(model)).thenReturn(Optional.of(mockedModel));

      command.run(options);

      Path project = outputDirectory.toPath().resolve(projectName);
      Path build = project.resolve("build.gradle");
      Path settings = project.resolve("settings.gradle");
      assertTrue(Files.isRegularFile(build));
      assertTrue(Files.isRegularFile(settings));
   }

   private static IBuildDependency newDependency(String groupId,
                                                 String artifactId,
                                                 String version,
                                                 String versionProperty) {
      IBuildDependency d = mock(IBuildDependency.class);
      when(d.getGroupId()).thenReturn(groupId);
      when(d.getArtifactId()).thenReturn(artifactId);
      when(d.getVersion()).thenReturn(version);
      when(d.getVersionPropertyName()).thenReturn(versionProperty);
      return d;
   }

   private static IProjectInformation newProject(String directoryName, String artifactId) {
      IProjectInformation project = mock(IProjectInformation.class);
      when(project.getDirectoryName()).thenReturn(directoryName);
      when(project.getArtifactId()).thenReturn(artifactId);
      return project;
   }
}
