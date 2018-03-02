package com.ngc.seaside.jellyfish.cli.command.createprotocolbuffermessages;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedBuildManagementService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedDataFieldGenerationService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedDataService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedPackageNamingService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedProjectNamingService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedTemplateService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.utilities.command.JellyfishCommandPhase;
import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.test.systemdescriptor.ModelUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ngc.seaside.jellyfish.cli.command.createprotocolbuffermessages.CreateProtocolbufferMessagesCommand.MESSAGES_BUILD_TEMPLATE_SUFFIX;
import static com.ngc.seaside.jellyfish.cli.command.createprotocolbuffermessages.CreateProtocolbufferMessagesCommand.MESSAGES_PROTO_TEMPLATE_SUFFIX;
import static com.ngc.seaside.jellyfish.cli.command.createprotocolbuffermessages.CreateProtocolbufferMessagesCommand.OUTPUT_DIRECTORY_PROPERTY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CreateProtocolbufferMessagesCommandIT {

   private final CreateProtocolbufferMessagesCommand cmd = new CreateProtocolbufferMessagesCommand();

   @Mock
   private IJellyFishCommandOptions options;

   private DefaultParameterCollection parameters = new DefaultParameterCollection();

   private Path outputDirectory;

   @Before
   public void setup() throws IOException {
      ITemplateService templateService = new MockedTemplateService()
            .useRealPropertyService()
            .setTemplateDirectory(
                  CreateProtocolbufferMessagesCommand.class.getPackage().getName() + "-"
                  + MESSAGES_BUILD_TEMPLATE_SUFFIX,
                  Paths.get("src", "main", "templates", "build"))
            .setTemplateDirectory(
                  CreateProtocolbufferMessagesCommand.class.getPackage().getName() + "-"
                  + MESSAGES_PROTO_TEMPLATE_SUFFIX,
                  Paths.get("src", "main", "templates", "proto"));

      cmd.setLogService(mock(ILogService.class));
      cmd.setPackageNamingService(new MockedPackageNamingService());
      cmd.setProjectNamingService(new MockedProjectNamingService());
      cmd.setDataFieldGenerationService(new MockedDataFieldGenerationService());
      cmd.setDataService(new MockedDataService());
      cmd.setBuildManagementService(new MockedBuildManagementService());
      cmd.setTemplateService(templateService);

      outputDirectory = Files.createTempDirectory(null);
      parameters.addParameter(new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, outputDirectory));
      parameters.addParameter(new DefaultParameter<>(CommonParameters.PHASE.getName(), JellyfishCommandPhase.DEFERRED));

      ISystemDescriptor systemDescriptor = mock(ISystemDescriptor.class);
      when(options.getParameters()).thenReturn(parameters);
      when(options.getSystemDescriptor()).thenReturn(systemDescriptor);

      IData base = ModelUtils.getMockNamedChild(IData.class, "com.ngc.Base");
      IData child = ModelUtils.getMockNamedChild(IData.class, "com.ngc.Child");
      IData data = ModelUtils.getMockNamedChild(IData.class, "com.ngc.Data");
      IEnumeration enumeration = ModelUtils.getMockNamedChild(IEnumeration.class, "com.ngc.Enumeration");
      ModelUtils.mockData(data,
                          null,
                          "dataField1",
                          DataTypes.INT,
                          "dataField2",
                          FieldCardinality.MANY,
                          DataTypes.STRING,
                          "dataField3",
                          FieldCardinality.MANY,
                          enumeration);
      ModelUtils.mockData(base,
                          null,
                          "baseField1",
                          DataTypes.INT,
                          "baseField2",
                          FieldCardinality.MANY, DataTypes.STRING,
                          "baseField3",
                          FieldCardinality.MANY,
                          enumeration,
                          "baseField4",
                          data);
      ModelUtils.mockData(child, base,
                          "childField1",
                          DataTypes.INT,
                          "childField2",
                          FieldCardinality.MANY,
                          DataTypes.STRING,
                          "childField3",
                          FieldCardinality.MANY,
                          enumeration,
                          "childField4",
                          data);
      ModelUtils.PubSubModel model = new ModelUtils.PubSubModel("com.ngc.Model");
      model.addPubSub("scenario1", "input1", data, "output1", child);
      when(systemDescriptor.findModel("com.ngc.Model")).thenReturn(Optional.of(model));
      parameters.addParameter(new DefaultParameter<>(CommonParameters.MODEL.getName(), model.getFullyQualifiedName()));
   }

   @Test
   public void testCommand() throws Exception {
      cmd.run(options);

      Path projectDirectory = outputDirectory.resolve("com.ngc.model.message");
      assertTrue("Project directory incorrect", Files.isDirectory(projectDirectory));

      Path gradleBuild = projectDirectory.resolve("build.generated.gradle");
      assertTrue(Files.isRegularFile(gradleBuild));

      Path sourceDirectory = projectDirectory.resolve(Paths.get("src", "main", "proto"));
      assertTrue(Files.isDirectory(sourceDirectory));

      List<Path> files = Files.walk(sourceDirectory)
            .filter(Files::isRegularFile)
            .sorted(Comparator.comparing(f -> f.getFileName().toString()))
            .collect(Collectors.toList());

      assertEquals(3, files.size());
      assertTrue(files.get(0).endsWith(Paths.get("com", "ngc", "child", "message", "Child.proto")));
      assertTrue(files.get(1).endsWith(Paths.get("com", "ngc", "data", "message", "Data.proto")));
      assertTrue(files.get(2).endsWith(Paths.get("com", "ngc", "enumeration", "message", "Enumeration.proto")));
   }

}
