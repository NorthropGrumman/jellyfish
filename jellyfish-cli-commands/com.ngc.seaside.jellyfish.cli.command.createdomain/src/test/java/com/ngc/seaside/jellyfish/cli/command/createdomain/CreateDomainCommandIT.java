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
package com.ngc.seaside.jellyfish.cli.command.createdomain;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.test.files.TestingFiles;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedPackageNamingService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedProjectNamingService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedTemplateService;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.user.api.IJellyfishUserService;
import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.test.systemdescriptor.ModelUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CreateDomainCommandIT {

   private CreateDomainCommand cmd;

   @Mock
   private IJellyFishCommandOptions options;

   @Mock
   private IBuildManagementService buildManagementService;

   private DefaultParameterCollection parameters = new DefaultParameterCollection();

   private Path outputDirectory;
   private Path velocityPath;

   @Before
   public void setup() throws IOException {
      cmd = new CreateDomainCommand();
      cmd.setLogService(mock(ILogService.class));
      cmd.setBuildManagementService(buildManagementService);
      cmd.setTemplateService(new MockedTemplateService().useRealPropertyService()
                                   .setTemplateDirectory(
                                         CreateDomainCommand.class.getPackage().getName(),
                                         Paths.get("src", "main", "template")));
      cmd.setJellyfishUserService(mock(IJellyfishUserService.class, Mockito.RETURNS_DEEP_STUBS));

      IPackageNamingService packageService = mock(IPackageNamingService.class);
      when(packageService.getDomainPackageName(any(), any())).thenAnswer(args -> {
         INamedChild<IPackage> element = args.getArgument(1);
         return element.getParent().getName() + ".domain";
      });
      cmd.setPackageNamingService(new MockedPackageNamingService());
      cmd.setProjectNamingService(new MockedProjectNamingService());

      outputDirectory = Files.createTempDirectory(null);
      outputDirectory.toFile().deleteOnExit();
      velocityPath = Paths.get("src", "test", "resources", "service-domain-source.vm");
      parameters.addParameter(new DefaultParameter<>(CommonParameters.OUTPUT_DIRECTORY.getName(), outputDirectory));
      parameters.addParameter(new DefaultParameter<>(CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY, velocityPath));

      ISystemDescriptor systemDescriptor = mock(ISystemDescriptor.class);
      when(options.getParameters()).thenReturn(parameters);
      when(options.getSystemDescriptor()).thenReturn(systemDescriptor);

      IData base = ModelUtils.getMockNamedChild(IData.class, "com.ngc.Base");
      IData child = ModelUtils.getMockNamedChild(IData.class, "com.ngc.Child");
      IData data = ModelUtils.getMockNamedChild(IData.class, "com.ngc.Data");
      IEnumeration enumeration = ModelUtils.getMockNamedChild(IEnumeration.class, "com.ngc.Enumeration");
      ModelUtils.mockData(data, null,
                          "field1",
                          DataTypes.INT,
                          "field2",
                          FieldCardinality.MANY,
                          DataTypes.STRING,
                          "field3",
                          FieldCardinality.MANY,
                          enumeration);
      ModelUtils.mockData(base, null,
                          "field1",
                          DataTypes.INT,
                          "field2",
                          FieldCardinality.MANY,
                          DataTypes.STRING,
                          "field3",
                          FieldCardinality.MANY,
                          enumeration,
                          "field4", data);
      ModelUtils.mockData(child, base,
                          "field1",
                          DataTypes.INT,
                          "field2",
                          FieldCardinality.MANY,
                          DataTypes.STRING,
                          "field3",
                          FieldCardinality.MANY,
                          enumeration,
                          "field4", data);
      ModelUtils.PubSubModel model = new ModelUtils.PubSubModel("com.ngc.Model");
      model.addInput("input1", data);
      model.addInput("input2", child);
      when(systemDescriptor.findModel("com.ngc.Model")).thenReturn(Optional.of(model));
      parameters.addParameter(new DefaultParameter<>(CommonParameters.MODEL.getName(), model.getFullyQualifiedName()));

      when(buildManagementService.registerDependency(options, CreateDomainCommand.BLOCS_PLUGINS_DEPENDENCY))
            .thenReturn(null);
   }

   @Test
   public void testCommand() throws Exception {
      cmd.run(options);

      Path projectDir = outputDirectory.resolve("com.ngc.model.domain");
      assertTrue("Cannot find project directory: " + projectDir, Files.isDirectory(projectDir));
      checkGradleBuild(projectDir, "com.ngc.base.domain", "com.ngc.child.domain", "com.ngc.data.domain",
                       "com.ngc.enumeration.domain");
      checkVelocity(projectDir);

      Path domain = projectDir.resolve(CreateDomainCommand.DOMAIN_PATH).resolve("com.ngc.Model.xml");
      assertTrue(Files.isRegularFile(domain));

      verify(buildManagementService).registerDependency(options, CreateDomainCommand.BLOCS_PLUGINS_DEPENDENCY);
   }

   private void checkGradleBuild(Path projectDir, String... fileContents) throws IOException {
      Path buildFile = projectDir.resolve("build.gradle");
      assertTrue("build.gradle is missing", Files.isRegularFile(buildFile));
      TestingFiles.assertFileContains(buildFile, velocityPath.getFileName().toString());
      for (String content : fileContents) {
         TestingFiles.assertFileContains(buildFile, content);
      }
   }

   private void checkVelocity(Path projectDir) {
      Path velocityFolder = projectDir.resolve(Paths.get("src", "main", "resources", "velocity"));
      assertTrue("Could not find velocity folder", Files.isDirectory(velocityFolder));
      assertTrue("Could not find velocity file: " + velocityPath.getFileName(),
                 Files.isRegularFile(velocityFolder.resolve(velocityPath.getFileName())));
   }

}
