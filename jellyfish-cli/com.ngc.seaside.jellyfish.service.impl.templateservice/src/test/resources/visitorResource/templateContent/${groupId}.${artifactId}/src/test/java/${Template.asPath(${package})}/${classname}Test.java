/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
