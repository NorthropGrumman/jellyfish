/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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

import java.util.Arrays;
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
}
