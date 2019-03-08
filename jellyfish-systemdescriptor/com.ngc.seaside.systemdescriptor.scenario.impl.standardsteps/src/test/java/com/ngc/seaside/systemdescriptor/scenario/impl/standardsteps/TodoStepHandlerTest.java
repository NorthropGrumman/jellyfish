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

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TodoStepHandlerTest {

   private TodoStepHandler handler;

   private IScenarioStep step;

   @Mock
   private IValidationContext<IScenarioStep> context;

   @Before
   public void setup() throws Throwable {
      handler = new TodoStepHandler();

      step = new ScenarioStep();
      step.setKeyword(TodoStepHandler.FUTURE.getVerb());

      when(context.getObject()).thenReturn(step);
   }

   @Test
   public void testDoesRegisterVerbs() throws Throwable {
      assertEquals("did not register correct past tense!",
                   TodoStepHandler.PAST,
                   handler.getVerbs().get(VerbTense.PAST_TENSE));
      assertEquals("did not register correct present tense!",
                   TodoStepHandler.PRESENT,
                   handler.getVerbs().get(VerbTense.PRESENT_TENSE));
      assertEquals("did not register correct future tense!",
                   TodoStepHandler.FUTURE,
                   handler.getVerbs().get(VerbTense.FUTURE_TENSE));
   }

   @Test
   public void testDoesValidateValidUsage() throws Throwable {
      when(context.declare(any(), anyString(), eq((IScenarioStep) step))).thenReturn(step);
      step.getParameters().addAll(Arrays.asList("finish", "something"));
      handler.doValidateStep(context);
      verify(context, times(1)).declare(eq(Severity.WARNING), anyString(), eq(step));
   }

}
