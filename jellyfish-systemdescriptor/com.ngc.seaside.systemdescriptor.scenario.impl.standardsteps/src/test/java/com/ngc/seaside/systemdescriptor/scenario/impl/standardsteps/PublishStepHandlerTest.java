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
package com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps;

import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.model.impl.basic.data.Data;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.DataReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.Model;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.scenario.Scenario;
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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PublishStepHandlerTest {

   private PublishStepHandler handler;

   private ScenarioStep step;

   private Data data;

   private DataReferenceField field;

   private Model model;

   @Mock
   private IValidationContext<IScenarioStep> context;

   @Before
   public void setup() {
      data = new Data("TestData");

      field = new DataReferenceField("output1");
      field.setType(data);

      model = new Model("TestModel");
      model.addOutput(field);
      model.addOutput(new DataReferenceField("out2").setType(data));

      Scenario scenario = new Scenario("test");
      scenario.setParent(model);

      step = new ScenarioStep();
      step.setKeyword(PublishStepHandler.PAST.getVerb());
      step.getParameters().add(field.getName());
      step.setParent(scenario);
      when(context.getObject()).thenReturn(step);

      handler = new PublishStepHandler();
   }

   @Test
   public void testDoesRegisterVerbs() {
      assertEquals("did not register correct past tense!",
                   PublishStepHandler.PAST,
                   handler.getVerbs().get(VerbTense.PAST_TENSE));
      assertEquals("did not register correct present tense!",
                   PublishStepHandler.PRESENT,
                   handler.getVerbs().get(VerbTense.PRESENT_TENSE));
      assertEquals("did not register correct future tense!",
                   PublishStepHandler.FUTURE,
                   handler.getVerbs().get(VerbTense.FUTURE_TENSE));
   }

   @Test
   public void testDoesValidateMissingParameters() {
      step.getParameters().clear();

      IScenarioStep mockedStep = mock(IScenarioStep.class);
      when(context.declare(eq(Severity.ERROR), anyString(), eq((IScenarioStep) step))).thenReturn(mockedStep);

      handler.doValidateStep(context);
      verify(mockedStep).getParameters();
   }

   @Test
   public void testDoesValidateWrongNumberOfParameters() {
      step.getParameters().add(field.getName());

      IScenarioStep mockedStep = mock(IScenarioStep.class);
      when(context.declare(eq(Severity.ERROR), anyString(), eq((IScenarioStep) step))).thenReturn(mockedStep);

      handler.doValidateStep(context);
      verify(mockedStep).getParameters();
   }

   @Test
   public void testDoesRequireFieldToBeAnOutput() {
      model.getOutputs().clear();
      model.addInput(field);

      IScenarioStep mockedStep = mock(IScenarioStep.class);
      when(context.declare(eq(Severity.ERROR), anyString(), eq((IScenarioStep) step))).thenReturn(mockedStep);

      handler.doValidateStep(context);
      verify(mockedStep).getParameters();
   }

   @Test
   public void testDoesRequireFieldNameToBeValid() {
      step.getParameters().clear();
      step.getParameters().add("foo");

      IScenarioStep mockedStep = mock(IScenarioStep.class);
      when(context.declare(eq(Severity.ERROR), anyString(), eq((IScenarioStep) step))).thenReturn(mockedStep);

      handler.doValidateStep(context);
      verify(mockedStep).getParameters();
   }

   @Test
   public void testAllowValidUsage() {
      handler.doValidateStep(context);
      verify(context, never()).declare(any(Severity.class), anyString(), any(IScenarioStep.class));
   }

   @Test
   public void testGetGetOutputs() {
      assertEquals("did not return correct data!",
                   field,
                   handler.getOutputs(step));
   }

   @Test
   public void testParameterCompletion() throws Throwable {
      List<String> suggestions;

      step.getParameters().clear();
      step.getParameters().addAll(Arrays.asList(""));
      suggestions = new ArrayList<>(handler.getSuggestedParameterCompletions(step, PublishStepHandler.FUTURE, 0));
      assertEquals(Arrays.asList("out2", "output1"), suggestions);

      step.getParameters().clear();
      step.getParameters().addAll(Arrays.asList("outp"));
      suggestions = new ArrayList<>(handler.getSuggestedParameterCompletions(step, PublishStepHandler.FUTURE, 0));
      assertEquals(Arrays.asList("output1"), suggestions);
   }
}
