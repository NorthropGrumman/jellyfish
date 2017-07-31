package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase;

import com.ngc.blocs.service.log.api.ILogService;

import org.junit.Before;
import org.junit.Test;

public class CreateJavaServiceBaseCommandTest {

   private CreateJavaServiceBaseCommand command;

   private ILogService logger;

   @Before
   public void setup() {
      command = new CreateJavaServiceBaseCommand();
      command.setLogService(logger);
   }

   @Test
   public void testCommand() {
      // TODO Auto-generated method stub
   }

}
