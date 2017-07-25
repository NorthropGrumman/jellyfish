package com.ngc.seaside.jellyfish.cli.command.createjavaservicecommand;

import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.bootstrap.service.impl.templateservice.TemplateService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateOutput;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.command.api.IParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.impl.basic.SystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.Model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateJavaServiceCommandTest {

   private CreateJavaServiceCommand fixture;
   private IPromptUserService promptUserService = mock(IPromptUserService.class);
   private IJellyFishCommandOptions options = mock(IJellyFishCommandOptions.class);
   private ISystemDescriptor systemDescriptor = mock(SystemDescriptor.class);
   private ITemplateService templateService = mock(TemplateService.class);
   private IModel model = mock(Model.class);

   private IParameterCollection addProjectParameters;

   @Before
   public void setup() throws IOException {
      // Setup mock system descriptor
      when(options.getSystemDescriptor()).thenReturn(systemDescriptor);
      when(systemDescriptor.findModel("com.ngc.seaside.test.Model")).thenReturn(Optional.of(model));

      // Setup mock model
      when(model.getParent()).thenReturn(mock(IPackage.class));
      when(model.getParent().getName()).thenReturn("com.ngc.seaside.test");
      when(model.getName()).thenReturn("Model");

      // Setup class under test
      fixture = new CreateJavaServiceCommand() {
         @Override
         protected void doAddProject(IParameterCollection parameters) {
            addProjectParameters = parameters;
         }
      };
      fixture.setLogService(new PrintStreamLogService());
      fixture.setPromptService(promptUserService);
      //fixture.setTemplateService(templateService);
   }

   @Test
   public void testCommandWithoutOptionalParams() {
      runCommand(CreateJavaServiceCommand.MODEL_PROPERTY, "com.ngc.seaside.test.Model",
                 CreateJavaServiceCommand.OUTPUT_DIRECTORY_PROPERTY, "/just/a/mock/path");

      // Verify mocked behaviors
      verify(options, times(1)).getParameters();
      verify(options, times(1)).getSystemDescriptor();
      verify(model, times(2)).getName();
      verify(model, times(2)).getParent();

      // Verify passed values
      Assert.assertEquals("com.ngc.seaside.test.Model",
                          addProjectParameters.getParameter(CreateJavaServiceCommand.MODEL_PROPERTY)
                                .getStringValue());
      Assert.assertEquals("/just/a/mock/path",
                          addProjectParameters.getParameter(CreateJavaServiceCommand.OUTPUT_DIRECTORY_PROPERTY)
                                .getStringValue());
      Assert.assertEquals(model.getName().toLowerCase() + ".distribution",
                          addProjectParameters.getParameter(CreateJavaServiceCommand.ARTIFACT_ID_PROPERTY)
                                .getStringValue());
      Assert.assertEquals(model.getParent().getName(),
                          addProjectParameters.getParameter(CreateJavaServiceCommand.GROUP_ID_PROPERTY)
                                .getStringValue());
   }

   @Test
   public void testCommandWithOptionalParams() {
      runCommand(CreateJavaServiceCommand.GENERATE_DELEGATE_PROPERTY, "true",
                 CreateJavaServiceCommand.GENERATE_BASE_PROPERTY, "false",
                 CreateJavaServiceCommand.OUTPUT_DIRECTORY_PROPERTY, "/just/a/mock/path",
                 CreateJavaServiceCommand.MODEL_PROPERTY, "com.ngc.seaside.test.Model",
                 CreateJavaServiceCommand.ARTIFACT_ID_PROPERTY, "model",
                 CreateJavaServiceCommand.GROUP_ID_PROPERTY, "com.ngc.seaside.test");

      // Verify mocked behaviors

      verify(options, times(1)).getParameters();
      verify(options, times(1)).getSystemDescriptor();
      verify(model, times(1)).getName();
      verify(model, times(1)).getParent();

      // Verify passed values
      Assert.assertEquals("true",
                          addProjectParameters.getParameter(CreateJavaServiceCommand.GENERATE_DELEGATE_PROPERTY)
                                .getStringValue());
      Assert.assertEquals("false",
                          addProjectParameters.getParameter(CreateJavaServiceCommand.GENERATE_BASE_PROPERTY)
                                .getStringValue());
      Assert.assertEquals("com.ngc.seaside.test.Model",
                          addProjectParameters.getParameter(CreateJavaServiceCommand.MODEL_PROPERTY)
                                .getStringValue());
      Assert.assertEquals("/just/a/mock/path",
                          addProjectParameters.getParameter(CreateJavaServiceCommand.OUTPUT_DIRECTORY_PROPERTY)
                                .getStringValue());
      Assert.assertEquals("model", addProjectParameters.getParameter(CreateJavaServiceCommand.ARTIFACT_ID_PROPERTY).getStringValue());

      Assert.assertEquals("com.ngc.seaside.test",
                          addProjectParameters.getParameter(CreateJavaServiceCommand.GROUP_ID_PROPERTY)
                                .getStringValue());
   }

   @Test
   public void testCommandWithoutRequiredParams() {

      // Mock behaviors
      when(promptUserService.prompt(CreateJavaServiceCommand.OUTPUT_DIRECTORY_PROPERTY, null, null))
            .thenReturn("/just/a/mock/path");
      when(promptUserService.prompt(CreateJavaServiceCommand.MODEL_PROPERTY, null, null))
            .thenReturn("com.ngc.seaside.test.Model");
      when(model.getName()).thenReturn("Model");

      runCommand(CreateJavaServiceCommand.ARTIFACT_ID_PROPERTY, "test",
                 CreateJavaServiceCommand.GROUP_ID_PROPERTY, "com.ngc.seaside");

      // Verify mocked behaviors
      verify(options, times(1)).getParameters();
      verify(options, times(1)).getSystemDescriptor();
      verify(promptUserService, times(1)).prompt(CreateJavaServiceCommand.MODEL_PROPERTY, null, null);
      verify(promptUserService, times(1)).prompt(CreateJavaServiceCommand.OUTPUT_DIRECTORY_PROPERTY, null, null);

      // Verify passed values
      Assert.assertEquals("true",
                          addProjectParameters.getParameter(CreateJavaServiceCommand.GENERATE_DELEGATE_PROPERTY)
                                .getStringValue());
      Assert.assertEquals("true",
                          addProjectParameters.getParameter(CreateJavaServiceCommand.GENERATE_BASE_PROPERTY)
                                .getStringValue());
      Assert.assertEquals("/just/a/mock/path",
                          addProjectParameters.getParameter(CreateJavaServiceCommand.OUTPUT_DIRECTORY_PROPERTY)
                                .getStringValue()
      );
      Assert.assertEquals("com.ngc.seaside.test.Model",
                          addProjectParameters.getParameter(CreateJavaServiceCommand.MODEL_PROPERTY)
                                .getStringValue());
      Assert.assertEquals("test", addProjectParameters.getParameter(CreateJavaServiceCommand.ARTIFACT_ID_PROPERTY)
            .getStringValue());
      Assert.assertEquals("com.ngc.seaside",
                          addProjectParameters.getParameter(CreateJavaServiceCommand.GROUP_ID_PROPERTY)
                                .getStringValue());

   }

   private void runCommand(String... keyValues) {
      DefaultParameterCollection collection = new DefaultParameterCollection();

      for (int n = 0; n + 1 < keyValues.length; n += 2) {
         collection.addParameter(new DefaultParameter<String>(keyValues[n]).setValue(keyValues[n + 1]));
      }

      when(options.getParameters()).thenReturn(collection);

      // Setup mock template service
      when(templateService.unpack("JellyFishJavaService", collection, Paths.get("/just/a/mock/path"), false))
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
      fixture.run(options);
   }
}