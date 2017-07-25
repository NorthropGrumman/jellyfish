package com.ngc.seaside.jellyfish.cli.command.createjavadistribution;

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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateJavaDistributionCommandTest {

   private CreateJavaDistributionCommand fixture;
   private IPromptUserService promptUserService = mock(IPromptUserService.class);
   private IJellyFishCommandOptions options = mock(IJellyFishCommandOptions.class);
   private ISystemDescriptor systemDescriptor = mock(SystemDescriptor.class);
   private ITemplateService templateService = mock(TemplateService.class);
   private IModel model = mock(Model.class);
   private Path createDirectoriesPath;
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
      fixture = new CreateJavaDistributionCommand() {
         @Override
         protected void doAddProject(IParameterCollection parameters) {
            addProjectParameters = parameters;
         }

         @Override
         protected void doCreateDirectories(Path outputDirectory) {
           createDirectoriesPath = outputDirectory;
         }
      };

      fixture.setLogService(new PrintStreamLogService());
      fixture.setPromptService(promptUserService);
      fixture.setTemplateService(templateService);
   }

   @Test
   public void testCommandWithoutOptionalParams() {
      runCommand(CreateJavaDistributionCommand.MODEL_PROPERTY, "com.ngc.seaside.test.Model",
                 CreateJavaDistributionCommand.OUTPUT_DIRECTORY_PROPERTY, "/just/a/mock/path");

      // Verify mocked behaviors
      verify(options, times(1)).getParameters();
      verify(options, times(1)).getSystemDescriptor();
      verify(model, times(3)).getName();
      verify(model, times(2)).getParent();

      // Verify passed values
      Assert.assertEquals("com.ngc.seaside.test.Model",
                          addProjectParameters.getParameter(CreateJavaDistributionCommand.MODEL_PROPERTY)
                                   .getStringValue());
      Assert.assertEquals("/just/a/mock/path",
                          addProjectParameters.getParameter(CreateJavaDistributionCommand.OUTPUT_DIRECTORY_PROPERTY)
                                   .getStringValue());
      Assert.assertEquals(model.getName().toLowerCase() + ".distribution",
                          addProjectParameters.getParameter(CreateJavaDistributionCommand.ARTIFACT_ID_PROPERTY)
                                   .getStringValue());
      Assert.assertEquals(model.getParent().getName(),
                          addProjectParameters.getParameter(CreateJavaDistributionCommand.GROUP_ID_PROPERTY)
                                   .getStringValue());
   }

   @Test
   public void testCommandWithOptionalParams() {
      runCommand(CreateJavaDistributionCommand.MODEL_PROPERTY, "com.ngc.seaside.test.Model",
                 CreateJavaDistributionCommand.OUTPUT_DIRECTORY_PROPERTY, "/just/a/mock/path",
                 CreateJavaDistributionCommand.ARTIFACT_ID_PROPERTY, "model",
                 CreateJavaDistributionCommand.GROUP_ID_PROPERTY, "com.ngc.seaside.test");

      // Verify mocked behaviors
      verify(options, times(1)).getParameters();
      verify(options, times(1)).getSystemDescriptor();
      verify(model, times(2)).getName();
      verify(model, times(1)).getParent();

      // Verify passed values
      Assert.assertEquals("com.ngc.seaside.test.Model",
                          addProjectParameters.getParameter(CreateJavaDistributionCommand.MODEL_PROPERTY)
                                   .getStringValue());
      Assert.assertEquals("/just/a/mock/path",
                          addProjectParameters.getParameter(CreateJavaDistributionCommand.OUTPUT_DIRECTORY_PROPERTY)
                                   .getStringValue());
      Assert.assertEquals("model", addProjectParameters.getParameter(CreateJavaDistributionCommand.ARTIFACT_ID_PROPERTY)
               .getStringValue());
      Assert.assertEquals("com.ngc.seaside.test",
                          addProjectParameters.getParameter(CreateJavaDistributionCommand.GROUP_ID_PROPERTY)
                                   .getStringValue());
   }

   @Test
   public void testCommandWithoutRequiredParams() {

      // Mock behaviors
      when(promptUserService.prompt(CreateJavaDistributionCommand.OUTPUT_DIRECTORY_PROPERTY, null, null))
               .thenReturn("/just/a/mock/path");
      when(promptUserService.prompt(CreateJavaDistributionCommand.MODEL_PROPERTY, null, null))
               .thenReturn("com.ngc.seaside.test.Model");
      when(model.getName()).thenReturn("Model");

      runCommand(CreateJavaDistributionCommand.ARTIFACT_ID_PROPERTY, "test",
                 CreateJavaDistributionCommand.GROUP_ID_PROPERTY, "com.ngc.seaside");

      // Verify mocked behaviors
      verify(options, times(1)).getParameters();
      verify(options, times(1)).getSystemDescriptor();
      verify(promptUserService, times(1)).prompt(CreateJavaDistributionCommand.MODEL_PROPERTY, null, null);
      verify(promptUserService, times(1)).prompt(CreateJavaDistributionCommand.OUTPUT_DIRECTORY_PROPERTY, null, null);

      // Verify passed values
      Assert.assertEquals("/just/a/mock/path",
                          addProjectParameters.getParameter(CreateJavaDistributionCommand.OUTPUT_DIRECTORY_PROPERTY)
                                   .getStringValue()
      );
      Assert.assertEquals("com.ngc.seaside.test.Model",
                          addProjectParameters.getParameter(CreateJavaDistributionCommand.MODEL_PROPERTY)
                                   .getStringValue());
      Assert.assertEquals("test", addProjectParameters.getParameter(CreateJavaDistributionCommand.ARTIFACT_ID_PROPERTY)
               .getStringValue());
      Assert.assertEquals("com.ngc.seaside",
                          addProjectParameters.getParameter(CreateJavaDistributionCommand.GROUP_ID_PROPERTY)
                                   .getStringValue());

   }

   private void runCommand(String... keyValues) {
      DefaultParameterCollection collection = new DefaultParameterCollection();

      for (int n = 0; n + 1 < keyValues.length; n += 2) {
         collection.addParameter(new DefaultParameter<String>(keyValues[n]).setValue(keyValues[n + 1]));
      }

      when(options.getParameters()).thenReturn(collection);

      // Setup mock template service
      when(templateService.unpack(CreateJavaDistributionCommand.class.getPackage().getName(), collection,
                                  Paths.get("/just/a/mock/path"), false)).thenReturn(new ITemplateOutput() {
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
