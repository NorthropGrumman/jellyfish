package com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import com.ngc.seaside.systemdescriptor.ext.test.systemdescriptor.ModelUtils;
import com.ngc.seaside.systemdescriptor.ext.test.systemdescriptor.ModelUtils.PubSubModel;
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
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
import java.util.Optional;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CorrelateStepHandlerTest {

   private CorrelateStepHandler handler;

   private ScenarioStep step;

   @Mock
   private IValidationContext<IScenarioStep> context;

   @Before
   public void setup() throws Throwable {
      handler = new CorrelateStepHandler();
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

//   @Test
//   public void testDoesValidateValidUsage() throws Throwable {
//      step = new ScenarioStep();
//      step.setKeyword(CorrelateStepHandler.PRESENT.getVerb());
//      when(context.getObject()).thenReturn(step);
//
//      step.getParameters().addAll(Arrays.asList("a.foo", "to", "b.foo"));
//      handler.doValidateStep(context);
//      verify(context, never()).declare(eq(Severity.ERROR), anyString(), eq(step));
//   }

   @Test
   public void testValidPresentVerb() throws Throwable {
      step = new ScenarioStep();
      step.setKeyword(CorrelateStepHandler.PRESENT.getVerb());
      step.getParameters().addAll(Arrays.asList("InputDataType0.intField0", "to", "InputDataType1.intField1"));
      
      
      IData inputDataType0 = ModelUtils.getMockNamedChild(IData.class, "test.InputDataType0");
      IData inputDataType1 = ModelUtils.getMockNamedChild(IData.class, "test.InputDataType1");
      IData outputDataType0 = ModelUtils.getMockNamedChild(IData.class, "test.OutputDataType0");
      
      
      IDataField field0 = mock(IDataField.class);
     
      //TODO add this and repeat for all fields passed in below
//      mock(field1.getReferencedDataType()).thenReturn();
//      mock(field1.getName()).thenReturn();
      
      //ModelUtils.mockData(data[0], null, field0, "intField1", DataTypes.INT);
      ModelUtils.mockData(inputDataType0, null, "intField0", DataTypes.INT, "intField1", DataTypes.INT);
      ModelUtils.mockData(outputDataType0, null, "intField2", DataTypes.INT);
      

      
      PubSubModel model = new PubSubModel("com.Model");
      model.addInput("input0", inputDataType0);
      model.addOutput("output0", outputDataType0);
      IScenario scenarioParent = mock(IScenario.class);
      when(scenarioParent.getParent()).thenReturn(model);
      model.addScenario(scenarioParent);
      step.setParent(scenarioParent);
      when(context.getObject()).thenReturn(step);
      
      
      INamedChildCollection<IModel, IDataReferenceField> inputs = handler.getInputs(step);
      INamedChildCollection<IModel, IDataReferenceField> outputs = handler.getOutputs(step);
      
      IDataReferenceField dataRefFieldInput = inputs.getByName("input0").get();
      IDataReferenceField dataRefFieldOutput = outputs.getByName("output0").get();
      
      IData inputData = dataRefFieldInput.getType();
      System.out.println(inputData.getName());
      
      for (IDataField val : inputData.getFields()) {
         System.out.println(val.getName());
         System.out.println(val.getType());
      }
      

      System.out.println();
      System.out.println();

   }


}
