package com.ngc.seaside.jellyfish.cli.command.samplecommand;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;

public class SampleCommandTest {

   private SampleCommand fixture;

   @Rule
   public TemporaryFolder testFolder = new TemporaryFolder();

   @Before
   public void setup() {
      fixture = new SampleCommand();
      fixture.setLogService(mock(ILogService.class));
      fixture.activate();
   }

   @After
   public void shutdown() {
      fixture.deactivate();
      fixture.setLogService(null);
   }

   @Test
   public void isNameCorrect() {
      assertEquals("sample", fixture.getName());
   }

   @Test
   public void isUsageCorrect() {
      IUsage usage = fixture.getUsage();

      //just assert that all of my parameters are actually required.
      assertEquals(5, usage.getRequiredParameters().size());
      assertEquals(5, usage.getAllParameters().size());
   }

   @Test(expected = CommandException.class)
   public void doesRun() throws IOException {
      //the run method doesn't do anything but set the test up so that this test fails if functionality is added
      //at a later date.
      IJellyFishCommandOptions options = mock(IJellyFishCommandOptions.class);

      IParameterCollection collection = mock(IParameterCollection.class);
      when(options.getParameters()).thenReturn(collection);
      fixture.run(options);
   }

}
