package com.ngc.seaside.jellyfish.cli.command.samplecommand;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.bootstrap.api.IBootstrapCommandOptions;
import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.IParameter;
import com.ngc.seaside.command.api.IParameterCollection;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.samplecommand.SampleCommand;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 */
public class SampleCommandTest {

   private ILogService logService;
   private SampleCommand fixture;

   @Rule
   public TemporaryFolder testFolder = new TemporaryFolder();

   @Before
   public void setup() {
      logService = new PrintStreamLogService();
      fixture = new SampleCommand();

      fixture.setLogService(logService);
      fixture.activate();
   }

   @After
   public void shutdown() {
      fixture.deactivate();
      fixture.removeLogService(logService);
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

      verify(options, times(1)).getParameters();
   }

   private IParameter createParameterMock(String name, String value) {
      IParameter mock = mock(IParameter.class);
      when(mock.getName()).thenReturn(name);
      when(mock.getValue()).thenReturn(value);
      return mock;
   }

}
