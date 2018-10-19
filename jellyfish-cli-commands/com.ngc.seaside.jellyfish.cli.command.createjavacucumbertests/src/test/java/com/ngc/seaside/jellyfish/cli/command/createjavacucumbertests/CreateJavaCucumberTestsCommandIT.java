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
package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertests;

import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedBuildManagementService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedTemplateService;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.EnumDto;
import com.ngc.seaside.jellyfish.service.config.api.ITelemetryConfigurationService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.jellyfish.service.user.api.IJellyfishUserService;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateJavaCucumberTestsCommandIT {

   private static final PrintStreamLogService logger = new PrintStreamLogService();

   private CreateJavaCucumberTestsCommand command = new CreateJavaCucumberTestsCommand();

   private MockedTemplateService templateService;

   private DefaultParameterCollection parameters;

   @Rule
   public final TemporaryFolder tempFolder = new TemporaryFolder();

   private Path outputDirectory;

   @Mock
   private IJellyFishCommandOptions options;

   @Mock
   private ISystemDescriptor systemDescriptor;

   @Mock
   private IModel model;

   @Mock
   private IJavaServiceGenerationService generationService;

   @Mock
   private IProjectNamingService projectService;

   @Mock
   private IPackageNamingService packageService;

   @Mock
   private ITelemetryConfigurationService telemetryConfigService;

   private IBuildManagementService buildManagementService;

   @Before
   public void setup() throws IOException {
      outputDirectory = tempFolder.newFolder().toPath();

      buildManagementService = new MockedBuildManagementService();

      templateService = new MockedTemplateService()
            .useRealPropertyService()
            .setTemplateDirectory(
                  CreateJavaCucumberTestsCommand.class.getPackage().getName() + "-"
                  + CreateJavaCucumberTestsCommand.BUILD_TEMPLATE_SUFFIX,
                  Paths.get("src", "main", "templates",
                            CreateJavaCucumberTestsCommand.BUILD_TEMPLATE_SUFFIX))
            .setTemplateDirectory(
                  CreateJavaCucumberTestsCommand.class.getPackage().getName() + "-"
                  + CreateJavaCucumberTestsCommand.CONFIG_TEMPLATE_SUFFIX,
                  Paths.get("src", "main", "templates",
                            CreateJavaCucumberTestsCommand.CONFIG_TEMPLATE_SUFFIX));

      parameters = new DefaultParameterCollection();
      when(options.getParameters()).thenReturn(parameters);

      // Setup mock system descriptor
      when(options.getSystemDescriptor()).thenReturn(systemDescriptor);

      command.setLogService(logger);
      command.setTemplateService(templateService);
      command.setProjectNamingService(projectService);
      command.setPackageNamingService(packageService);
      command.setJavaServiceGenerationService(generationService);
      command.setBuildManagementService(buildManagementService);
      command.setTelemetryConfigService(telemetryConfigService);
      command.setJellyfishUserService(mock(IJellyfishUserService.class, Mockito.RETURNS_DEEP_STUBS));
      command.activate();
   }

   private void setupModel(Path inputDirectory, String pkg, String name) {
      when(systemDescriptor.findModel(pkg + '.' + name)).thenReturn(Optional.of(model));
      when(model.getName()).thenReturn(name);
      when(model.getFullyQualifiedName()).thenReturn(pkg + '.' + name);

      IProjectInformation info = mock(IProjectInformation.class);
      when(projectService.getCucumberTestsProjectName(any(), eq(model))).thenReturn(info);
      when(info.getDirectoryName()).thenReturn(pkg + "." + name.toLowerCase() + ".tests");

      info = mock(IProjectInformation.class);
      when(projectService.getBaseServiceProjectName(any(), eq(model))).thenReturn(info);
      when(info.getArtifactId()).thenReturn(name.toLowerCase() + ".base.impl");

      info = mock(IProjectInformation.class);
      when(projectService.getMessageProjectName(any(), eq(model))).thenReturn(info);
      when(info.getArtifactId()).thenReturn(name.toLowerCase() + ".messages");

      when(packageService.getCucumberTestsPackageName(any(), eq(model)))
            .thenReturn(pkg + "." + name.toLowerCase() + ".tests");

      EnumDto mockEnum = mock(EnumDto.class);
      when(generationService.getTransportTopicsDescription(any(), eq(model))).thenReturn(mockEnum);
      when(mockEnum.getFullyQualifiedName())
            .thenReturn(pkg + "." + name.toLowerCase() + ".transport.topics." + name + "TransportTopics");
   }

   @Test
   public void testDoesGenerateANewProject() throws IOException, URISyntaxException {
      Path inputDir = Paths.get("src", "test", "resources");
      setupModel(inputDir, "com.ngc.seaside.testeval", "HamburgerService");

      parameters.addParameter(new DefaultParameter<>(CreateJavaCucumberTestsCommand.MODEL_PROPERTY,
                                                     model.getFullyQualifiedName()));
      parameters.addParameter(new DefaultParameter<>(CreateJavaCucumberTestsCommand.OUTPUT_DIRECTORY_PROPERTY,
                                                     outputDirectory.toString()));

      command.run(options);

      Path projectDir = outputDirectory.resolve(model.getFullyQualifiedName().toLowerCase() + ".tests");

      Path javaDir = projectDir.resolve(
            Paths.get("src",
                      "main",
                      "java",
                      model.getFullyQualifiedName().replace('.', File.separatorChar).toLowerCase(),
                      "tests"));
      Assert.assertTrue(Files.isDirectory(javaDir));

   }

}
