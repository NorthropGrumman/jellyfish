package com.ngc.seaside.jellyfish.cli.command.createjavapubsubconnector;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedDataFieldGenerationService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedPackageNamingService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedProjectNamingService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedRequirementsService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedScenarioService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedTransportConfigurationService;
import com.ngc.seaside.jellyfish.cli.command.test.template.MockedTemplateService;
import com.ngc.seaside.systemdescriptor.ext.test.systemdescriptor.ModelUtils;
import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateJavaPubsubConnectorCommandIT {

   private CreateJavaPubsubConnectorCommand cmd = new CreateJavaPubsubConnectorCommand();
   private IJellyFishCommandOptions options = mock(IJellyFishCommandOptions.class);
   private DefaultParameterCollection parameters = new DefaultParameterCollection();

   private Path outputDirectory;

   @Before
   public void setup() throws IOException {
      cmd.setLogService(mock(ILogService.class));
      cmd.setPackageNamingService(new MockedPackageNamingService());
      cmd.setProjectNamingService(new MockedProjectNamingService());
      cmd.setRequirementsService(new MockedRequirementsService());
      cmd.setDataFieldGenerationService(new MockedDataFieldGenerationService());
      cmd.setJavaServiceGenerationService(new MockedJavaServiceGenerationService());
      cmd.setScenarioService(new MockedScenarioService());
      cmd.setTransportConfigurationService(new MockedTransportConfigurationService());
      cmd.setTemplateService(new MockedTemplateService().useRealPropertyService()
         .setTemplateDirectory(CreateJavaPubsubConnectorCommand.class.getPackage().getName(),
                                                           Paths.get("src", "main", "template")));

      outputDirectory = Files.createTempDirectory(null);
      parameters.addParameter(
         new DefaultParameter<>(CreateJavaPubsubConnectorCommand.OUTPUT_DIRECTORY_PROPERTY, outputDirectory));

      ISystemDescriptor systemDescriptor = mock(ISystemDescriptor.class);
      when(options.getParameters()).thenReturn(parameters);
      when(options.getSystemDescriptor()).thenReturn(systemDescriptor);

      IData base = ModelUtils.getMockNamedChild(IData.class, "com.ngc.Base");
      IData child = ModelUtils.getMockNamedChild(IData.class, "com.ngc.Child");
      IData data = ModelUtils.getMockNamedChild(IData.class, "com.ngc.Data");
      IEnumeration enumeration = ModelUtils.getMockNamedChild(IEnumeration.class, "com.ngc.Enumeration");
      ModelUtils.mockData(data, null, "dataField1", DataTypes.INT, "dataField2", FieldCardinality.MANY, DataTypes.STRING, "dataField3", FieldCardinality.MANY, enumeration);
      ModelUtils.mockData(base, null, "baseField1", DataTypes.INT, "baseField2", FieldCardinality.MANY, DataTypes.STRING, "baseField3", FieldCardinality.MANY, enumeration, "baseField4", data);
      ModelUtils.mockData(child, base, "childField1", DataTypes.INT, "childField2", FieldCardinality.MANY, DataTypes.STRING, "childField3", FieldCardinality.MANY, enumeration, "childField4", data);
      ModelUtils.PubSubModel model = new ModelUtils.PubSubModel("com.ngc.Model");
      model.addPubSub("scenario1", "input1", data, "output1", child);
      when(systemDescriptor.findModel("com.ngc.Model")).thenReturn(Optional.of(model));
      parameters.addParameter(new DefaultParameter<>(CommonParameters.MODEL.getName(), model.getFullyQualifiedName()));

   }

   @Test
   public void testCommand() throws IOException {
      cmd.run(options);
      Path srcFolder = outputDirectory.resolve(
         Paths.get("com.ngc.model.connector", "src", "main", "java", "com", "ngc", "model", "connector"));
      Path connectorFile = srcFolder.resolve("ModelConnector.java");
      Path conversionFile = srcFolder.resolve("ModelDataConversion.java");

      assertTrue(Files.isRegularFile(connectorFile));

      Set<String> convertStatements = new HashSet<>(Arrays.asList("Child", "Data", "Enumeration"));
      Set<String> setAddStatements = new HashSet<>(
         Arrays.asList("setBaseField1", "addBaseField2", "addBaseField3", "setBaseField4",
            "setChildField1", "addChildField2", "addChildField3", "setChildField4",
            "setDataField1", "addDataField2", "addDataField3"));
      Pattern convertPattern = Pattern.compile("\\bconvert\\s*\\(\\s*\\S*([A-Z]\\w*)\\b");
      Pattern setAddPattern = Pattern.compile("\\b((?:set|add)[A-Z]\\w*)\\b");
      Pattern invalidType = Pattern.compile("<\\s*(?:boolean|int|long|float|double)\\s*>");

      Files.lines(conversionFile).forEach(line -> {
         Matcher m = convertPattern.matcher(line);
         if (m.find()) {
            convertStatements.remove(m.group(1));
         }
         m = setAddPattern.matcher(line);
         if (m.find()) {
            setAddStatements.remove(m.group(1));
         }
         m = invalidType.matcher(line);
         if (m.find()) {
            fail("Invalid type parameter: " + line);
         }
      });
      
      assertTrue(convertStatements.toString(), convertStatements.isEmpty());
      assertTrue(setAddStatements.toString(), setAddStatements.isEmpty());
   }

}
