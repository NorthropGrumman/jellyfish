package com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps;

import com.ngc.seaside.systemdescriptor.model.impl.basic.data.Data;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.DataReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.Model;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.scenario.Scenario;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.scenario.ScenarioStep;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ReceiveStepHandlerTest {

   private ReceiveStepHandler handler;

   private ScenarioStep step;

   private Data data;

   private DataReferenceField field;

   @Before
   public void setup() throws Throwable {
      data = new Data("TestData");

      field = new DataReferenceField("input1");
      field.setType(data);

      Model model = new Model("TestModel");
      model.addInput(field);

      Scenario scenario = new Scenario("test");
      scenario.setParent(model);

      step = new ScenarioStep();
      step.setKeyword(ReceiveStepHandler.PAST.getVerb());
      step.getParameters().add(field.getName());
      step.setParent(scenario);

      handler = new ReceiveStepHandler();
   }

   @Test
   public void testGetGetInputs() throws Throwable {
      assertEquals("did not return correct field!",
                   field,
                   handler.getInputs(step));
   }
}
