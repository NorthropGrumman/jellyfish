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
package com.ngc.seaside.jellyfish.service.scenario.impl.scenarioservice.processor;

import com.ngc.seaside.jellyfish.service.scenario.api.IRequestResponseMessagingFlow;
import com.ngc.seaside.systemdescriptor.model.impl.basic.data.Data;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.DataReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.Model;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.scenario.Scenario;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.scenario.ScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.ReceiveRequestStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.RespondStepHandler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RequestResponseProcessorTest {

   private RequestResponseProcessor processor;

   private Scenario scenario;

   private DataReferenceField output;

   private DataReferenceField input;

   @Mock
   private ReceiveRequestStepHandler receiveRequestStepHandler;

   @Mock
   private RespondStepHandler respondStepHandler;

   @Before
   public void setup() {
      Data outputData = new Data("TestData1");
      Data inputData = new Data("TestData2");

      output = new DataReferenceField("myOutput");
      input = new DataReferenceField("myInput");
      output.setType(outputData);
      input.setType(inputData);

      Model model = new Model("TestModel");
      model.addOutput(output);
      model.addInput(input);

      scenario = new Scenario("getTestScenario");
      scenario.setParent(model);

      ScenarioStep receiveRequestStep = new ScenarioStep();
      receiveRequestStep.setKeyword(ReceiveRequestStepHandler.PRESENT.getVerb());
      receiveRequestStep.getParameters().add(input.getName());
      receiveRequestStep.setParent(scenario);
      scenario.addWhen(receiveRequestStep);

      ScenarioStep respondStep = new ScenarioStep();
      respondStep.setKeyword(RespondStepHandler.FUTURE.getVerb());
      respondStep.getParameters().add("with");
      respondStep.getParameters().add(output.getName());
      respondStep.setParent(scenario);
      scenario.addThen(respondStep);

      when(receiveRequestStepHandler.getRequest(receiveRequestStep)).thenReturn(input);
      when(respondStepHandler.getResponse(respondStep)).thenReturn(output);

      processor = new RequestResponseProcessor(receiveRequestStepHandler, respondStepHandler);
   }

   @Test
   public void testDoesComputeServerFlows() {
      Optional<IRequestResponseMessagingFlow> optionalFlow = processor.getFlow(scenario);
      assertTrue("expected a flow!", optionalFlow.isPresent());

      IRequestResponseMessagingFlow flow = optionalFlow.get();
      assertEquals("flow type not correct!",
                   IRequestResponseMessagingFlow.FlowType.SERVER,
                   flow.getFlowType());
      assertEquals("input not correct!",
                   input,
                   flow.getInput());
      assertEquals("output not correct!",
                   output,
                   flow.getOutput());
      assertEquals("scenario is not correct!",
                   scenario,
                   flow.getScenario());
   }

   @Test
   public void testDoesComputeMessagingParadigmsForServerFlows() {
      assertTrue("scenario should be req/res!",
                 processor.isRequestResponse(scenario));

      scenario.getWhens().clear();
      scenario.getThens().clear();
      assertFalse("scenario should not be req/res!",
                  processor.isRequestResponse(scenario));
   }
}
