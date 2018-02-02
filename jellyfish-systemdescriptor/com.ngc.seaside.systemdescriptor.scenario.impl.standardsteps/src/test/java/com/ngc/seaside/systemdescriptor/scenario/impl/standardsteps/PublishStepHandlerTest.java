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

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class PublishStepHandlerTest {

   private PublishStepHandler handler;

   private ScenarioStep step;

   private Data data;

   private DataReferenceField field;

   @Before
   public void setup() throws Throwable {
      data = new Data("TestData");

      field = new DataReferenceField("output1");
      field.setType(data);

      Model model = new Model("TestModel");
      model.addOutput(field);

      Scenario scenario = new Scenario("test");
      scenario.setParent(model);

      step = new ScenarioStep();
      step.setKeyword(PublishStepHandler.PAST.getVerb());
      step.getParameters().add(field.getName());
      step.setParent(scenario);

      handler = new PublishStepHandler();
   }

   @Test
   public void testGetGetOutputs() throws Throwable {
      assertEquals("did not return correct data!",
                   field,
                   handler.getOutputs(step));
   }
}
