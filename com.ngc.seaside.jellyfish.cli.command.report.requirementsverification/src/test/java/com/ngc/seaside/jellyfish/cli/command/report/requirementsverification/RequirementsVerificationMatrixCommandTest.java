package com.ngc.seaside.jellyfish.cli.command.report.requirementsverification;

import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RequirementsVerificationMatrixCommandTest {

   private RequirementsVerificationMatrixCommand cmd = new RequirementsVerificationMatrixCommand();

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
