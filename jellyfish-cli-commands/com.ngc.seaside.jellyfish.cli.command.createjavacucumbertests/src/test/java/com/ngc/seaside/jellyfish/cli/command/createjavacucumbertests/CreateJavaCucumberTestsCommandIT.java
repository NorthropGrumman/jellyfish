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
package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedBuildManagementService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedTemplateService;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.EnumDto;
import com.ngc.seaside.jellyfish.service.config.api.ITelemetryConfigurationService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.jellyfish.service.user.api.IJellyfishUserService;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.service.log.api.PrintStreamLogService;

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

      when(generationService.getServiceInterfaceDescription(any(), any())).thenReturn(
            new ClassDto().setTypeName("IFooService").setPackageName("a.b.c").setName("fooService")
      );

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
