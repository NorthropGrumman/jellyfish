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
package com.ngc.seaside.jellyfish.api;

import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultJellyFishCommandOptionsTest {

   private DefaultParameterCollection parameters;

   private DefaultJellyFishCommandOptions options;

   @Mock
   private IParsingResult parsingResult;

   @Mock
   private ISystemDescriptor systemDescriptor;

   @Before
   public void setup() throws Throwable {
      when(parsingResult.getSystemDescriptor()).thenReturn(systemDescriptor);

      parameters = new DefaultParameterCollection();
      parameters.addParameter(new DefaultParameter<>("param1", "foo"));

      options = new DefaultJellyFishCommandOptions();
      options.setParsingResult(parsingResult);
      options.setParameters(parameters);
   }

   @Test
   public void testDoesMergeOptions() throws Throwable {
      IJellyFishCommandOptions merged = DefaultJellyFishCommandOptions.mergeWith(
            options,
            new DefaultParameter<>("param2", "bar"));
      assertEquals("system descriptor not set!",
                   systemDescriptor,
                   merged.getSystemDescriptor());
      assertEquals("parsing result not set!",
                   parsingResult,
                   merged.getParsingResult());
      assertEquals("did not include existing param!",
                   "foo",
                   merged.getParameters().getParameter("param1").getValue());
      assertEquals("did not add param!",
                   "bar",
                   merged.getParameters().getParameter("param2").getValue());
   }

   @Test
   public void testDoesMergeAndOverrideOptions() throws Throwable {
      IJellyFishCommandOptions merged = DefaultJellyFishCommandOptions.mergeWith(
            options,
            new DefaultParameter<>("param1", "bar"));
      assertEquals("did not replace existing param!",
                   "bar",
                   merged.getParameters().getParameter("param1").getValue());
   }
}
