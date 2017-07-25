package com.ngc.seaside.jellyfish.cli.command.createjavaevents;

import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CreateJavaEventsCommandTest {

   private CreateJavaEventsCommand cmd = new CreateJavaEventsCommand();

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
