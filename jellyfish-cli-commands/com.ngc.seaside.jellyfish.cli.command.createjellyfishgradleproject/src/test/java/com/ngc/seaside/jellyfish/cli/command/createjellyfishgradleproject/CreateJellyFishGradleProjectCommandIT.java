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
package com.ngc.seaside.jellyfish.cli.command.createjellyfishgradleproject;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedTemplateService;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.DependencyScope;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.service.user.api.IJellyfishUserService;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;

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
      command.setJellyfishUserService(mock(IJellyfishUserService.class, Mockito.RETURNS_DEEP_STUBS));
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
