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
public class RespondStepHandlerTest {

   private RespondStepHandler handler;

   private ScenarioStep step;

   private Data data;

   private DataReferenceField field;

   private Scenario scenario;

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

      scenario = new Scenario("test");
      scenario.setParent(model);

      step = new ScenarioStep();
      step.setKeyword(RespondStepHandler.FUTURE.getVerb());
      step.getParameters().add("with");
      step.getParameters().add(field.getName());
      step.setParent(scenario);
      scenario.addThen(step);

      when(context.getObject()).thenReturn(step);

      handler = new RespondStepHandler();
   }

   @Test
   public void testDoesRegisterVerbs() {
      assertEquals("did not register correct past tense!",
                   RespondStepHandler.PAST,
                   handler.getVerbs().get(VerbTense.PAST_TENSE));
      assertEquals("did not register correct present tense!",
                   RespondStepHandler.PRESENT,
                   handler.getVerbs().get(VerbTense.PRESENT_TENSE));
      assertEquals("did not register correct future tense!",
                   RespondStepHandler.FUTURE,
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
   public void testDoesRequireWithParameter() {
      step.getParameters().clear();
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
   public void testDoesAllowOnlyOneRespondStepPerScenario() {
      ScenarioStep extraStep = new ScenarioStep();
      extraStep.setKeyword(RespondStepHandler.FUTURE.getVerb());
      scenario.addThen(extraStep);

      IScenarioStep mockedStep = mock(IScenarioStep.class);
      when(context.declare(eq(Severity.ERROR), anyString(), eq((IScenarioStep) extraStep))).thenReturn(mockedStep);

      handler.doValidateStep(context);
      verify(mockedStep).getKeyword();
   }

   @Test
   public void testAllowValidUsage() {
      handler.doValidateStep(context);
      verify(context, never()).declare(any(Severity.class), anyString(), any(IScenarioStep.class));
   }

   @Test
   public void testGetGetOutputs() {
      assertEquals("did not return correct field!",
                   field,
                   handler.getResponse(step));
   }

   @Test
   public void testParameterCompletion() throws Throwable {
      List<String> suggestions;

      step.getParameters().clear();
      step.getParameters().addAll(Arrays.asList("w"));
      suggestions =
               new ArrayList<>(handler.getSuggestedParameterCompletions(step, ReceiveRequestStepHandler.FUTURE, 0));
      assertEquals(Arrays.asList("with"), suggestions);

      step.getParameters().clear();
      step.getParameters().addAll(Arrays.asList("with", ""));
      suggestions =
               new ArrayList<>(handler.getSuggestedParameterCompletions(step, ReceiveRequestStepHandler.FUTURE, 1));
      assertEquals(Arrays.asList("out2", "output1"), suggestions);

      step.getParameters().clear();
      step.getParameters().addAll(Arrays.asList("with", "outp"));
      suggestions =
               new ArrayList<>(handler.getSuggestedParameterCompletions(step, ReceiveRequestStepHandler.FUTURE, 1));
      assertEquals(Arrays.asList("output1"), suggestions);
   }
}
