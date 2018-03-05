package com.ngc.seaside.jellyfish.cli.command.samplecommand;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class SampleCommandTest {

   private SampleCommand command;

   @Rule
   public TemporaryFolder temporaryFolder = new TemporaryFolder();

   @Before
   public void setup() {
      command = new SampleCommand();
      command.setLogService(mock(ILogService.class));
      command.activate();
   }

   @After
   public void shutdown() {
      command.deactivate();
      command.setLogService(null);
   }

   @Test
   public void isNameCorrect() {
      assertEquals("name not correct!",
                   "sample",
                   command.getName());
   }

   @Test
   public void isUsageCorrect() {
      IUsage usage = command.getUsage();

      assertEquals("number of required parameters of usage is incorrect!",
                   5,
                   usage.getRequiredParameters().size());
      assertEquals("total number of parameters of usage is incorrect!",
                   5,
                   usage.getAllParameters().size());
   }

   @Test
   public void doesRun() {
      IJellyFishCommandOptions options = mock(IJellyFishCommandOptions.class);
      IParameterCollection collection = mock(IParameterCollection.class);
      when(options.getParameters()).thenReturn(collection);
      command.run(options);

      // Do assertions and verifications here.
   }

}
