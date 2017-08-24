package com.ngc.seaside.jellyfish.cli.command.createjavaserviceconnector;

import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.bootstrap.service.impl.templateservice.TemplateService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateOutput;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.command.api.IParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.impl.basic.SystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.Model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import javax.json.JsonObject;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateJavaServiceConnectorCommandTest {

   private static final String TEST_MODEL = "com.ngc.seaside.test.Model";
   private CreateJavaServiceConnectorCommand cmd;
   private IPromptUserService promptUserService = mock(IPromptUserService.class);
   private IJellyFishCommandOptions options = mock(IJellyFishCommandOptions.class);
   private ISystemDescriptor systemDescriptor = mock(SystemDescriptor.class);
   private ITemplateService templateService = mock(TemplateService.class);
   private IModel model = mock(Model.class);
   private IParameterCollection parameters;

   @SuppressWarnings("unchecked")
   private INamedChildCollection<IModel, IDataReferenceField> inputs = mock(INamedChildCollection.class);
   @SuppressWarnings("unchecked")
   private INamedChildCollection<IModel, IDataReferenceField> outputs = mock(INamedChildCollection.class);
   
   @Before
   public void setup() throws IOException {
      // Setup mock system descriptor
      when(options.getSystemDescriptor()).thenReturn(systemDescriptor);
      when(systemDescriptor.findModel(TEST_MODEL)).thenReturn(Optional.of(model));

      // Setup mock model
      when(model.getParent()).thenReturn(mock(IPackage.class));
      when(model.getParent().getName()).thenReturn("com.ngc.seaside.test");
      when(model.getName()).thenReturn("Model");
      when(model.getMetadata()).thenReturn(mock(IMetadata.class));
      when(model.getMetadata().getJson()).thenReturn(mock(JsonObject.class));
      when(model.getInputs()).thenReturn(inputs);
      when(model.getOutputs()).thenReturn(outputs);

      // Setup class under test
      cmd = new CreateJavaServiceConnectorCommand() {
         @Override
         protected void doAddProject(IParameterCollection params) {
            parameters = params;
         }
      };
      cmd.setLogService(new PrintStreamLogService());
      cmd.setPromptService(promptUserService);
      cmd.setTemplateService(templateService);
   }

   @Test
   public void testCommandWithoutOptionalParams() {
      runCommand(CreateJavaServiceConnectorCommand.MODEL_PROPERTY, TEST_MODEL,
                 CreateJavaServiceConnectorCommand.OUTPUT_DIRECTORY_PROPERTY, "/just/a/mock/path");

      // Verify mocked behaviors
      verify(options, times(1)).getParameters();
      verify(options, times(2)).getSystemDescriptor();
      verify(model, times(2)).getName();
      verify(model, times(2)).getParent();

      // Verify passed values
      Assert.assertEquals(TEST_MODEL,
                          parameters.getParameter(CreateJavaServiceConnectorCommand.MODEL_PROPERTY)
                                .getStringValue());
      Assert.assertEquals("/just/a/mock/path",
                          parameters.getParameter(CreateJavaServiceConnectorCommand.OUTPUT_DIRECTORY_PROPERTY)
                                .getStringValue());
   }

   @Test
   public void testCommandWithOptionalParams() {
      runCommand(CreateJavaServiceConnectorCommand.MODEL_PROPERTY, TEST_MODEL,
                 CreateJavaServiceConnectorCommand.OUTPUT_DIRECTORY_PROPERTY, "/just/a/mock/path",
                 CreateJavaServiceConnectorCommand.ARTIFACT_ID_PROPERTY, "model",
                 CreateJavaServiceConnectorCommand.GROUP_ID_PROPERTY, "com.ngc.seaside.test");

      // Verify mocked behaviors
      verify(options, times(1)).getParameters();
      verify(options, times(2)).getSystemDescriptor();
      verify(model, times(1)).getName();
      verify(model, times(1)).getParent();

      // Verify passed values
      Assert.assertEquals(TEST_MODEL,
                          parameters.getParameter(CreateJavaServiceConnectorCommand.MODEL_PROPERTY)
                                .getStringValue());
      Assert.assertEquals("/just/a/mock/path",
                          parameters.getParameter(CreateJavaServiceConnectorCommand.OUTPUT_DIRECTORY_PROPERTY)
                                .getStringValue());
      Assert.assertEquals("model",
                          parameters.getParameter(CreateJavaServiceConnectorCommand.ARTIFACT_ID_PROPERTY)
                                 .getStringValue());
      Assert.assertEquals("com.ngc.seaside.test",
                          parameters.getParameter(CreateJavaServiceConnectorCommand.GROUP_ID_PROPERTY)
                                 .getStringValue());
   }

   @Test
   public void testCommandWithoutRequiredParams() {

      // Mock behaviors
      when(promptUserService.prompt(CreateJavaServiceConnectorCommand.OUTPUT_DIRECTORY_PROPERTY, null, null))
            .thenReturn("/just/a/mock/path");
      when(promptUserService.prompt(CreateJavaServiceConnectorCommand.MODEL_PROPERTY, null, null))
            .thenReturn(TEST_MODEL);
      when(model.getName()).thenReturn("Model");

      runCommand(CreateJavaServiceConnectorCommand.ARTIFACT_ID_PROPERTY, "test",
                 CreateJavaServiceConnectorCommand.GROUP_ID_PROPERTY, "com.ngc.seaside");

      // Verify mocked behaviors
      verify(options, times(1)).getParameters();
      verify(options, times(2)).getSystemDescriptor();
      verify(promptUserService, times(1)).prompt(CreateJavaServiceConnectorCommand.MODEL_PROPERTY, null, null);
      verify(promptUserService, times(1)).prompt(CreateJavaServiceConnectorCommand.OUTPUT_DIRECTORY_PROPERTY, null, null);

      // Verify passed values
      Assert.assertEquals("/just/a/mock/path",
                          parameters.getParameter(CreateJavaServiceConnectorCommand.OUTPUT_DIRECTORY_PROPERTY)
                                    .getStringValue()
      );
      Assert.assertEquals(TEST_MODEL,
                          parameters.getParameter(CreateJavaServiceConnectorCommand.MODEL_PROPERTY)
                                .getStringValue());

      Assert.assertEquals("test",
                          parameters.getParameter(CreateJavaServiceConnectorCommand.ARTIFACT_ID_PROPERTY)
                                 .getStringValue());

      Assert.assertEquals("com.ngc.seaside",
                          parameters.getParameter(CreateJavaServiceConnectorCommand.GROUP_ID_PROPERTY)
                                .getStringValue());

   }

   private void runCommand(String... keyValues) {
      DefaultParameterCollection collection = new DefaultParameterCollection();

      for (int n = 0; n + 1 < keyValues.length; n += 2) {
         collection.addParameter(new DefaultParameter<String>(keyValues[n]).setValue(keyValues[n + 1]));
      }

      when(options.getParameters()).thenReturn(collection);

      // Setup mock template service
      when(templateService.unpack("JellyFishJavaDistribution", collection, Paths.get("/just/a/mock/path"), false))
            .thenReturn(
                  new ITemplateOutput() {
                     @Override
                     public Map<String, ?> getProperties() {
                        return collection.getParameterMap();
                     }

                     @Override
                     public Path getOutputPath() {
                        return Paths.get("/just/a/mock/path");
                     }
                  });
      cmd.run(options);
   }
}