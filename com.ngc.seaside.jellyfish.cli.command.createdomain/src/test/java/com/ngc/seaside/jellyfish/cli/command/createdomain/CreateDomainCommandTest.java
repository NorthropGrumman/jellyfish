package com.ngc.seaside.jellyfish.cli.command.createdomain;

import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CreateDomainCommandTest {

   private CreateDomainCommand cmd = new CreateDomainCommand();

   private PrintStreamLogService logger = new PrintStreamLogService();

   @Before
   public void setup() {
      cmd.setLogService(logger);
   }

   @Test
   public void testCommand() {
      // TODO Auto-generated method stub
   }
   
}
