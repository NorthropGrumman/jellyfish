/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package ${package};

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.api.IUsage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ${classname}Test {

   private ILogService logService;
   private ${classname} fixture;

   @Before
   public void setup() {
      logService = new PrintStreamLogService();
      fixture = new ${classname}();

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
      assertEquals("${commandName}", fixture.getName());
   }

   @Test
   public void isUsageCorrect() {
      IUsage usage = fixture.getUsage();

      assertTrue("The usage description must not be null.", usage.getDescription() != null);
      assertFalse("The usage description must not be empty.", usage.getDescription().trim().isEmpty());

      //just assert that all of my parameters are actually required.
      assertEquals(5, usage.getRequiredParameters().size());
      assertEquals(5, usage.getAllParameters().size());
   }

   @Test(expected = CommandException.class)
   public void testRun() {
      //The run method only uses already existing functionality to add the new command
      //to the settings.gradle file. Just ensure that if we don't add any options that the
      //exception is thrown for this test.

      IJellyFishCommandOptions options = mock(IJellyFishCommandOptions.class);

      IParameterCollection collection = mock(IParameterCollection.class);
      when(options.getParameters()).thenReturn(collection);
      fixture.run(options);

      verify(options, times(1)).getParameters();
   }
}
