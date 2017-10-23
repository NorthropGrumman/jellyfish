package com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps;

import static org.junit.Assert.assertEquals;

import com.ngc.seaside.systemdescriptor.model.impl.basic.data.Data;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.DataReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.Model;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.scenario.Scenario;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.scenario.ScenarioStep;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CorrelateStepHandlerTest {

   private CorrelateStepHandler handler;

   private ScenarioStep step;

   private Data data;

   private DataReferenceField outputField;
   
   private DataReferenceField inputField;

   @Before
   public void setup() throws Throwable {
      data = new Data("TestData");

      outputField = new DataReferenceField("output1");
      outputField.setType(data);
      
      inputField = new DataReferenceField("input1");
      inputField.setType(data);

      Model model = new Model("TestModel");
      model.addOutput(outputField);
      model.addInput(inputField);

      Scenario scenario = new Scenario("test");
      scenario.setParent(model);

      step = new ScenarioStep();
      step.setKeyword(CorrelateStepHandler.PRESENT.getVerb());
      step.getParameters().add(outputField.getName());
      step.getParameters().add(inputField.getName());
      step.setParent(scenario);

      handler = new CorrelateStepHandler();
   }

   @Test
   public void testGetOutputs() throws Throwable {
      assertEquals("did not return correct data!",
         outputField,
         handler.getOutputs(step));
   }
   
   @Test
   public void testGetInputs() throws Throwable {
      assertEquals("did not return correct data!",
         inputField,
         handler.getInputs(step));
   }
}
