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
   public void setup() throws Throwable {
      when(validationContext.getObject()).thenReturn(step);
      when(validationContext.declare(any(), anyString(), any())).thenReturn(step);

      handler = new TestableStepHandler();
   }

   @Test
   public void testDoesRegisterVerbs() throws Throwable {
      assertEquals("did not store past tense!",
                   handler.PAST.getVerb(),
                   handler.getVerbs().get(VerbTense.PAST_TENSE).getVerb());
      assertEquals("did not store present tense!",
                   handler.PRESENT.getVerb(),
                   handler.getVerbs().get(VerbTense.PRESENT_TENSE).getVerb());
      assertEquals("did not store future tense!",
                   handler.FUTURE.getVerb(),
                   handler.getVerbs().get(VerbTense.FUTURE_TENSE).getVerb());
   }

   @Test
   public void testDoesRequireStepParameters() throws Throwable {
      handler.requireStepParameters(validationContext, "my error");

      when(step.getParameters()).thenReturn(Collections.singletonList("blah"));
      handler.requireStepParameters(validationContext, "my error");

      verify(validationContext, times(1)).declare(Severity.ERROR, "my error", step);
   }

   @Test
   public void testOnlyValidateStepIfStepUsesCorrectVerb() throws Throwable {
      when(step.getKeyword())
            .thenReturn(handler.PAST.getVerb())
            .thenReturn(handler.PRESENT.getVerb())
            .thenReturn(handler.FUTURE.getVerb())
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

   private class TestableStepHandler extends AbstractStepHandler {

      private final ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("tested");
      private final ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("testing");
      private final ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("will test");

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
