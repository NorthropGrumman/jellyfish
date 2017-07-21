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
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class CreateJavaDistributionCommandTest {
   private CreateJavaDistributionCommand fixture = new CreateJavaDistributionCommand();
   private ILogService logService;
   private IPromptUserService promptUserService = Mockito.mock(IPromptUserService.class);
   private IJellyFishCommandOptions options = Mockito.mock(IJellyFishCommandOptions.class);
   private ISystemDescriptor sd = Mockito.mock(SystemDescriptor.class);
   private IModel model = Mockito.mock(Model.class);
   private Path outputDir;

   @Before
   public void setup() throws IOException {
      logService = new PrintStreamLogService();
      fixture.setLogService(logService);
      outputDir = Files.createTempDirectory(null);
      outputDir.toFile().deleteOnExit();

      // Setup system descriptor
      Mockito.when(options.getSystemDescriptor()).thenReturn(sd);
      Mockito.when(sd.findModel("model")).thenReturn(Optional.of(model));
   }

   @Test
   public void testCommand() {
      Mockito.when(model.getParent()).thenReturn(Mockito.mock(IPackage.class));
      Mockito.when(model.getParent().getName()).thenReturn("com.ngc.seaside");
      Mockito.when(model.getName()).thenReturn("Model");

      runCommand(CreateJavaDistributionCommand.MODEL_PROPERTY, "com.ngc.seaside.test.Model",
                 CreateJavaDistributionCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString());

      Mockito.verify(fixture, Mockito.times(1)).getModelProperty(options, options.getParameters());
      Mockito.verify(fixture, Mockito.times(1)).
      Mockito.verify(fixture, Mockito.times(1)).getModelProperty(options, options.getParameters());
      Mockito.verify(fixture, Mockito.times(1)).getModelProperty(options, options.getParameters());

      Mockito.verify(options, Mockito.times(1)).getParameters();
      Mockito.verify(options, Mockito.times(1)).getSystemDescriptor();
   }

   private void runCommand(String... keyValues) {
      DefaultParameterCollection collection = new DefaultParameterCollection();

      for (int n = 0; n + 1 < keyValues.length; n += 2) {
         collection.addParameter(new DefaultParameter(keyValues[n]).setValue(keyValues[n + 1]));
      }

      Mockito.when(options.getParameters()).thenReturn(collection);

      fixture.run(options);
   }
}
