package com.ngc.seaside.jellyfish.cli.command.createjavadistribution;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.impl.basic.SystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.Model;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateJavaDistributionCommandTest {
   private CreateJavaDistributionCommand fixture = new CreateJavaDistributionCommand();
   private ILogService logService;
   private IPromptUserService promptUserService = mock(IPromptUserService.class);
   private IJellyFishCommandOptions options = mock(IJellyFishCommandOptions.class);
   private ISystemDescriptor systemDescriptor = mock(SystemDescriptor.class);
   private IModel model = mock(Model.class);

   @Before
   public void setup() throws IOException {
      // Setup test resources
      logService = new PrintStreamLogService();

      // Setup mock system descriptor
      when(options.getSystemDescriptor()).thenReturn(systemDescriptor);
      when(systemDescriptor.findModel("com.ngc.seaside.test.Model")).thenReturn(Optional.of(model));

      // Setup mock model
      when(model.getParent()).thenReturn(mock(IPackage.class));
      when(model.getParent().getName()).thenReturn("com.ngc.seaside");
      when(model.getName()).thenReturn("Model");

      // Setup class under test
      fixture.setLogService(logService);
      fixture.setPromptService(promptUserService);
   }

   @Test
   public void testCommandWithoutOptionalParams() {

      runCommand(CreateJavaDistributionCommand.MODEL_PROPERTY, "com.ngc.seaside.test.Model",
                 CreateJavaDistributionCommand.OUTPUT_DIRECTORY_PROPERTY, "/just/a/mock/path");

      verify(options, times(1)).getParameters();
      verify(options, times(1)).getSystemDescriptor();
      verify(model, times(2)).getName();
      verify(model, times(2)).getParent();
   }

   @Test
   public void testCommandWithOptionalParams() {

      runCommand(CreateJavaDistributionCommand.MODEL_PROPERTY, "com.ngc.seaside.test.Model",
                 CreateJavaDistributionCommand.OUTPUT_DIRECTORY_PROPERTY, "/just/a/mock/path",
                 CreateJavaDistributionCommand.ARTIFACT_ID_PROPERTY, "test",
                 CreateJavaDistributionCommand.GROUP_ID_PROPERTY, "com.ngc.seaside");

      verify(options, times(1)).getParameters();
      verify(options, times(1)).getSystemDescriptor();

   }

   @Test
   public void testCommandWithoutRequiredParams() {
      when(promptUserService.prompt(CreateJavaDistributionCommand.OUTPUT_DIRECTORY_PROPERTY, null, null))
               .thenReturn("/just/a/mock/path");
      when(promptUserService.prompt(CreateJavaDistributionCommand.MODEL_PROPERTY, null, null))
               .thenReturn("com.ngc.seaside.test.Model");
      when(model.getName()).thenReturn("Model");

      runCommand(CreateJavaDistributionCommand.ARTIFACT_ID_PROPERTY, "test",
                 CreateJavaDistributionCommand.GROUP_ID_PROPERTY, "com.ngc.seaside");

      verify(options, times(1)).getParameters();
      verify(options, times(1)).getSystemDescriptor();
      verify(promptUserService, times(1)).prompt(CreateJavaDistributionCommand.MODEL_PROPERTY, null, null);
      verify(promptUserService, times(1)).prompt(CreateJavaDistributionCommand.OUTPUT_DIRECTORY_PROPERTY, null, null);
   }

   private void runCommand(String... keyValues) {
      DefaultParameterCollection collection = new DefaultParameterCollection();

      for (int n = 0; n + 1 < keyValues.length; n += 2) {
         collection.addParameter(new DefaultParameter(keyValues[n]).setValue(keyValues[n + 1]));
      }

      DefaultParameterCollection test = mock(DefaultParameterCollection.class);

      when(options.getParameters()).thenReturn(collection);
      fixture.run(options);
   }
}
