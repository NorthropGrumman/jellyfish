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
package com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CompleteStepHandlerTest {

   private CompleteStepHandler handler;

   private IScenarioStep step;

   @Mock
   private IValidationContext<IScenarioStep> context;

   @Before
   public void setup() throws Throwable {
      handler = new CompleteStepHandler();

      step = new ScenarioStep();
      step.setKeyword(CompleteStepHandler.FUTURE.getVerb());

      when(context.getObject()).thenReturn(step);
   }

   @Test
   public void testDoesRegisterVerbs() throws Throwable {
      assertEquals("did not register correct past tense!",
                   CompleteStepHandler.PAST,
                   handler.getVerbs().get(VerbTense.PAST_TENSE));
      assertEquals("did not register correct present tense!",
                   CompleteStepHandler.PRESENT,
                   handler.getVerbs().get(VerbTense.PRESENT_TENSE));
      assertEquals("did not register correct future tense!",
                   CompleteStepHandler.FUTURE,
                   handler.getVerbs().get(VerbTense.FUTURE_TENSE));
   }

   @Test
   public void testDoesValidateValidUsage() throws Throwable {
      step.getParameters().addAll(Arrays.asList("within", "500", TimeUnit.SECONDS.toString().toLowerCase()));
      handler.doValidateStep(context);
      verify(context, never()).declare(eq(Severity.ERROR), anyString(), eq(step));
   }

   @Test
   public void testDoesValidateWrongNumberOfParameters() throws Throwable {
      IScenarioStep mockedStep = mock(IScenarioStep.class);
      when(context.declare(eq(Severity.ERROR), anyString(), eq(step))).thenReturn(mockedStep);

      handler.doValidateStep(context);
      verify(mockedStep).getKeyword();
   }

   @Test
   public void testDoesValidateWrongOperand() throws Throwable {
      IScenarioStep mockedStep = mock(IScenarioStep.class);
      when(context.declare(eq(Severity.ERROR), anyString(), eq(step))).thenReturn(mockedStep);
      step.getParameters().addAll(Arrays.asList("somewhereAround", "500", TimeUnit.SECONDS.toString().toLowerCase()));

      handler.doValidateStep(context);
      verify(mockedStep).getKeyword();
   }

   @Test
   public void testDoesValidateWrongDuration() throws Throwable {
      IScenarioStep mockedStep = mock(IScenarioStep.class);
      when(context.declare(eq(Severity.ERROR), anyString(), eq(step))).thenReturn(mockedStep);
      step.getParameters().addAll(Arrays.asList("within", "aWhile", TimeUnit.SECONDS.toString().toLowerCase()));

      handler.doValidateStep(context);
      verify(mockedStep).getKeyword();
   }

   @Test
   public void testDoesValidateWrongTimeUnit() throws Throwable {
      IScenarioStep mockedStep = mock(IScenarioStep.class);
      when(context.declare(eq(Severity.ERROR), anyString(), eq(step))).thenReturn(mockedStep);
      step.getParameters().addAll(Arrays.asList("within", "500", "centuries"));

      handler.doValidateStep(context);
      verify(mockedStep).getKeyword();
   }

   @Test
   public void testDoesGetValues() throws Throwable {
      step.getParameters().addAll(Arrays.asList("within", "500", TimeUnit.SECONDS.toString().toLowerCase()));

      assertEquals("operand not correct!",
                   CompleteStepHandler.Operand.LESS_THAN_OR_EQUAL,
                   handler.getOperand(step));
      assertEquals("duration not correct!",
                   500,
                   handler.getDuration(step),
                   Double.MIN_VALUE);
      assertEquals("time unit not correct!",
                   TimeUnit.SECONDS,
                   handler.getTimeUnit(step));
   }

   @Test
   public void testParameterCompletion() throws Throwable {
      List<String> suggestions;

      step.getParameters().clear();
      step.getParameters().addAll(Arrays.asList(""));
      suggestions = new ArrayList<>(handler.getSuggestedParameterCompletions(step, CompleteStepHandler.FUTURE, 0));
      assertEquals(Arrays.asList("atLeast", "within"), suggestions);

      step.getParameters().clear();
      step.getParameters().addAll(Arrays.asList("w"));
      suggestions = new ArrayList<>(handler.getSuggestedParameterCompletions(step, CompleteStepHandler.FUTURE, 0));
      assertEquals(Collections.singletonList("within"), suggestions);

      step.getParameters().clear();
      step.getParameters().addAll(Arrays.asList("within", "5"));
      suggestions = new ArrayList<>(handler.getSuggestedParameterCompletions(step, CompleteStepHandler.FUTURE, 1));
      assertEquals(Collections.emptyList(), suggestions);

      step.getParameters().clear();
      step.getParameters().addAll(Arrays.asList("within", "5", "m"));
      suggestions = new ArrayList<>(handler.getSuggestedParameterCompletions(step, CompleteStepHandler.FUTURE, 2));
      assertEquals(Arrays.asList("microseconds", "milliseconds", "minutes"), suggestions);
   }
}
