package com.ngc.seaside.jellyfish.cli.command.createjavadistribution;

import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CreateJavaDistributionCommandTest {

   private CreateJavaDistributionCommand cmd = new CreateJavaDistributionCommand();

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
