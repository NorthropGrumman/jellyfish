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
package com.ngc.seaside.jellyfish.cli.command.createjavaprotobufconnector;

import static com.ngc.seaside.jellyfish.cli.command.createjavaprotobufconnector.CreateJavaProtobufConnectorCommand.PUBSUB_BUILD_TEMPLATE_SUFFIX;
import static com.ngc.seaside.jellyfish.cli.command.createjavaprotobufconnector.CreateJavaProtobufConnectorCommand.PUBSUB_GENBUILD_TEMPLATE_SUFFIX;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedBuildManagementService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedDataFieldGenerationService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedPackageNamingService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedProjectNamingService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedRequirementsService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedScenarioService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedTemplateService;
import com.ngc.seaside.jellyfish.cli.command.test.service.config.MockedTransportConfigurationService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.service.user.api.IJellyfishUserService;
import com.ngc.seaside.jellyfish.utilities.command.JellyfishCommandPhase;
import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.test.systemdescriptor.ModelUtils;

public class CreateJavaProtobufConnectorCommandIT {

   private CreateJavaProtobufConnectorCommand cmd = new CreateJavaProtobufConnectorCommand();
   private IJellyFishCommandOptions options = mock(IJellyFishCommandOptions.class);
   private DefaultParameterCollection parameters = new DefaultParameterCollection();

   private Path outputDirectory;

   @Before
   public void setup() throws IOException {
      ITemplateService templateService = new MockedTemplateService()
            .useRealPropertyService()
            .setTemplateDirectory(
                  CreateJavaProtobufConnectorCommand.class.getPackage().getName() + "-"
                  + PUBSUB_GENBUILD_TEMPLATE_SUFFIX,
                  Paths.get("src", "main", "templates", "genbuild"))
            .setTemplateDirectory(
                  CreateJavaProtobufConnectorCommand.class.getPackage().getName() + "-"
                  + PUBSUB_BUILD_TEMPLATE_SUFFIX,
                  Paths.get("src", "main", "templates", "build"));

      cmd.setLogService(mock(ILogService.class));
      cmd.setPackageNamingService(new MockedPackageNamingService());
      cmd.setProjectNamingService(new MockedProjectNamingService());
      cmd.setRequirementsService(new MockedRequirementsService());
      cmd.setDataFieldGenerationService(new MockedDataFieldGenerationService());
      cmd.setJavaServiceGenerationService(new MockedJavaServiceGenerationService());
      cmd.setScenarioService(new MockedScenarioService());
      cmd.setTransportConfigurationService(new MockedTransportConfigurationService());
      cmd.setBuildManagementService(new MockedBuildManagementService());
      cmd.setTemplateService(templateService);
      cmd.setJellyfishUserService(mock(IJellyfishUserService.class, Mockito.RETURNS_DEEP_STUBS));

      outputDirectory = Files.createTempDirectory(null);
      parameters.addParameter(new DefaultParameter<>(CommonParameters.OUTPUT_DIRECTORY.getName(), outputDirectory));
      // Turn off the file header with an empty file.
      parameters.addParameter(new DefaultParameter<>(
            CommonParameters.HEADER_FILE.getName(),
            Files.createTempFile(CreateJavaProtobufConnectorCommandIT.class.getSimpleName(), "tests")));

      ISystemDescriptor systemDescriptor = mock(ISystemDescriptor.class);
      when(options.getParameters()).thenReturn(parameters);
      when(options.getSystemDescriptor()).thenReturn(systemDescriptor);

      IData base = ModelUtils.getMockNamedChild(IData.class, "com.ngc.Base");
      IData child = ModelUtils.getMockNamedChild(IData.class, "com.ngc.Child");
      IData data = ModelUtils.getMockNamedChild(IData.class, "com.ngc.Data");
      IData request = ModelUtils.getMockNamedChild(IData.class, "com.ngc.Request");
      IData response = ModelUtils.getMockNamedChild(IData.class, "com.ngc.Response");
      IEnumeration enumeration = ModelUtils.getMockNamedChild(IEnumeration.class, "com.ngc.Enumeration");

      ModelUtils.mockData(data, null,
                          "dataField1", DataTypes.INT,
                          "dataField2", FieldCardinality.MANY, DataTypes.STRING,
                          "dataField3", FieldCardinality.MANY, enumeration);
      ModelUtils.mockData(base, null,
                          "baseField1", DataTypes.INT,
                          "baseField2", FieldCardinality.MANY, DataTypes.STRING,
                          "baseField3", FieldCardinality.MANY, enumeration,
                          "baseField4", data);
      ModelUtils.mockData(child, base,
                          "childField1", DataTypes.INT,
                          "childField2", FieldCardinality.MANY, DataTypes.STRING,
                          "childField3", FieldCardinality.MANY, enumeration,
                          "childField4", data);
      ModelUtils.mockData(request, null,
                          "requestField1", DataTypes.INT,
                          "requestField2", FieldCardinality.MANY, DataTypes.STRING);
      ModelUtils.mockData(response, null,
                          "responseField1", DataTypes.INT,
                          "responseField2", FieldCardinality.MANY, DataTypes.STRING);

      ModelUtils.PubSubModel model = new ModelUtils.PubSubModel("com.ngc.Model");
      model.addPubSub("scenario1", "input1", data, "output1", child);
      ModelUtils.addReqRes(model,
                           "requestResponseScenario",
                           "request", request,
                           "response", response);

      when(systemDescriptor.findModel("com.ngc.Model")).thenReturn(Optional.of(model));

      parameters.addParameter(new DefaultParameter<>(CommonParameters.MODEL.getName(), model.getFullyQualifiedName()));

   }

   @Test
   public void testDoesRunDeferredPhase() throws IOException {
      parameters.addParameter(new DefaultParameter<>(CommonParameters.PHASE.getName(), JellyfishCommandPhase.DEFERRED));
      cmd.run(options);

      Path srcFolder = outputDirectory.resolve(
            Paths.get("com.ngc.model.connector", "src", "main", "java", "com", "ngc", "model", "connector"));
      Path connectorFile = srcFolder.resolve("ModelConnector.java");
      Path conversionFile = srcFolder.resolve("ModelDataConversion.java");

      assertTrue(Files.isRegularFile(connectorFile));

      Set<String> convertStatements = new HashSet<>(Arrays.asList("Child", "Data", "Enumeration"));
      Set<String> setAddStatements = new HashSet<>(Arrays.asList("setBaseField1",
                                                                 "addBaseField2",
                                                                 "addBaseField3",
                                                                 "setBaseField4",
                                                                 "setChildField1",
                                                                 "addChildField2",
                                                                 "addChildField3",
                                                                 "setChildField4",
                                                                 "setDataField1",
                                                                 "addDataField2",
                                                                 "addDataField3"));
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

   @Test
   public void testDoesRunDefaultPhase() throws Throwable {
      cmd.run(options);

      Path projectDirectory = outputDirectory.resolve("com.ngc.model.connector");
      Path gradleBuild = projectDirectory.resolve("build.gradle");
      assertTrue(Files.isRegularFile(gradleBuild));
   }
}
