package com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.scenario.ScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.api.VerbTense;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class CorrelateStepHandlerTest {

   private CorrelateStepHandler handler;

   private ScenarioStep step;

   @Mock
   private IValidationContext<IScenarioStep> context;

   @Before
   public void setup() throws Throwable {
      handler = new CorrelateStepHandler();

      step = new ScenarioStep();
      step.setKeyword(CorrelateStepHandler.FUTURE.getVerb());

      when(context.getObject()).thenReturn(step);
   }

   @Test
   public void testDoesRegisterVerbs() throws Throwable {

      assertEquals("did not register correct present tense!",
         CorrelateStepHandler.PRESENT,
         handler.getVerbs().get(VerbTense.PRESENT_TENSE));
      assertEquals("did not register correct future tense!",
         CorrelateStepHandler.FUTURE,
         handler.getVerbs().get(VerbTense.FUTURE_TENSE));
   }

   @Test
   public void testDoesValidateValidUsage() throws Throwable {
      step.getParameters().addAll(Arrays.asList("a.foo", "to", "b.foo"));
      handler.doValidateStep(context);
      verify(context, never()).declare(eq(Severity.ERROR), anyString(), eq(step));
   }

}
