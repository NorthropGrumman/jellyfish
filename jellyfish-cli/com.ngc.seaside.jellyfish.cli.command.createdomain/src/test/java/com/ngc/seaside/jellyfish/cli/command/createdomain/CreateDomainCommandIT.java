package com.ngc.seaside.jellyfish.cli.command.createdomain;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.test.files.TestingFiles;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedPackageNamingService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedProjectNamingService;
import com.ngc.seaside.jellyfish.cli.command.test.template.MockedTemplateService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.systemdescriptor.test.systemdescriptor.ModelUtils;
import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CreateDomainCommandIT {

   private CreateDomainCommand cmd;

   @Mock
   private IJellyFishCommandOptions options;

   private DefaultParameterCollection parameters = new DefaultParameterCollection();

   private Path outputDirectory;
   private Path velocityPath;

   @Before
   public void setup() throws IOException {
      cmd = new CreateDomainCommand();
      cmd.setLogService(mock(ILogService.class));
      cmd.setTemplateService(new MockedTemplateService().useRealPropertyService()
                                                        .setTemplateDirectory(
                                                           CreateDomainCommand.class.getPackage().getName(),
                                                           Paths.get("src", "main", "template")));

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
      ModelUtils.mockData(data, null, "field1", DataTypes.INT, "field2", FieldCardinality.MANY, DataTypes.STRING, "field3", FieldCardinality.MANY, enumeration);
      ModelUtils.mockData(base, null, "field1", DataTypes.INT, "field2", FieldCardinality.MANY, DataTypes.STRING, "field3", FieldCardinality.MANY, enumeration, "field4", data);
      ModelUtils.mockData(child, base, "field1", DataTypes.INT, "field2", FieldCardinality.MANY, DataTypes.STRING, "field3", FieldCardinality.MANY, enumeration, "field4", data);
      ModelUtils.PubSubModel model = new ModelUtils.PubSubModel("com.ngc.Model");
      model.addInput("input1", data);
      model.addInput("input2", child);
      when(systemDescriptor.findModel("com.ngc.Model")).thenReturn(Optional.of(model));
      parameters.addParameter(new DefaultParameter<>(CommonParameters.MODEL.getName(), model.getFullyQualifiedName()));
   }

   @Test
   public void testCommand() throws Exception {
      cmd.run(options);

      Path projectDir = outputDirectory.resolve("com.ngc.model.domain");
      assertTrue("Cannot find project directory: " + projectDir, Files.isDirectory(projectDir));
      checkGradleBuild(projectDir, "com.ngc.base.domain", "com.ngc.child.domain", "com.ngc.data.domain", "com.ngc.enumeration.domain");
      checkVelocity(projectDir);

      Path domain = projectDir.resolve(CreateDomainCommand.DOMAIN_PATH).resolve("com.ngc.Model.xml");
      assertTrue(Files.isRegularFile(domain));
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