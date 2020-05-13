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
package com.ngc.seaside.systemdescriptor.scenario.api;

import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AbstractStepHandlerTest {

   private TestableStepHandler handler;

   @Mock
   private IValidationContext<IScenarioStep> validationContext;

   @Mock
   private IScenarioStep step;

   @Before
   public void setup() {
      when(validationContext.getObject()).thenReturn(step);
      when(validationContext.declare(any(), anyString(), any())).thenReturn(step);

      handler = new TestableStepHandler();
   }

   @Test
   public void testDoesRegisterVerbs() {
      assertEquals("did not store past tense!",
                   TestableStepHandler.PAST.getVerb(),
                   handler.getVerbs().get(VerbTense.PAST_TENSE).getVerb());
      assertEquals("did not store present tense!",
                   TestableStepHandler.PRESENT.getVerb(),
                   handler.getVerbs().get(VerbTense.PRESENT_TENSE).getVerb());
      assertEquals("did not store future tense!",
                   TestableStepHandler.FUTURE.getVerb(),
                   handler.getVerbs().get(VerbTense.FUTURE_TENSE).getVerb());
   }

   @Test
   public void testDoesRequireStepParameters() {
      handler.requireStepParameters(validationContext, "my error");

      when(step.getParameters()).thenReturn(Collections.singletonList("blah"));
      handler.requireStepParameters(validationContext, "my error");

      verify(validationContext, times(1)).declare(Severity.ERROR, "my error", step);
   }

   @Test
   public void testOnlyValidateStepIfStepUsesCorrectVerb() {
      when(step.getKeyword())
            .thenReturn(TestableStepHandler.PAST.getVerb())
            .thenReturn(TestableStepHandler.PRESENT.getVerb())
            .thenReturn(TestableStepHandler.FUTURE.getVerb())
            .thenReturn("somethingElse");

      handler.validateStep(validationContext);
      assertTrue("validation of extending class was not invoked!",
                 handler.wasValidationInvoked);
      handler.reset();

      handler.validateStep(validationContext);
      assertTrue("validation of extending class was not invoked!",
                 handler.wasValidationInvoked);
      handler.reset();

      handler.validateStep(validationContext);
      assertTrue("validation of extending class was not invoked!",
                 handler.wasValidationInvoked);
      handler.reset();

      handler.validateStep(validationContext);
      assertFalse("validation of extending class should not have been invoked!",
                  handler.wasValidationInvoked);
   }

   private static class TestableStepHandler extends AbstractStepHandler {

      private static final ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("tested");
      private static final ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("testing");
      private static final ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("will test");

      private boolean wasValidationInvoked = false;

      private TestableStepHandler() {
         register(PAST, PRESENT, FUTURE);
      }

      @Override
      protected void doValidateStep(IValidationContext<IScenarioStep> context) {
         wasValidationInvoked = true;
      }

      private void reset() {
         wasValidationInvoked = false;
      }
   }
}
