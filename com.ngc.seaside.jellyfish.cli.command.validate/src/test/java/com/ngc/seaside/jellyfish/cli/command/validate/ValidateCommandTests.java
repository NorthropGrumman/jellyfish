package com.ngc.seaside.jellyfish.cli.command.validate;

import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ValidateCommandTests {

   private ValidateCommand cmd = new ValidateCommand();
   
   private PrintStreamLogService logger = new PrintStreamLogService();
   
   private IJellyFishCommandOptions options = Mockito.mock(IJellyFishCommandOptions.class);
   
   private ISystemDescriptor sd = Mockito.mock(ISystemDescriptor.class);
   
   @Before
   public void before() {
      cmd.setLogService(logger);
   }
   
   @Test
   public void testNonNullSystemDescriptor() {
      Mockito.when(options.getSystemDescriptor()).thenReturn(sd);
      cmd.run(options);
   }
   
   @Test(expected = Exception.class)
   public void testNullSystemDescriptor() {
      cmd.run(options);
   }
   
}
