package com.ngc.seaside.jellyfish.service.scenario.impl.scenarioservice.processor;

import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow;
import com.ngc.seaside.systemdescriptor.model.impl.basic.data.Data;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.DataReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.Model;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.scenario.Scenario;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.scenario.ScenarioStep;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario.PublishStepHandler;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario.ReceiveStepHandler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PubSubProcessorTest {

   private PubSubProcessor processor;

   private Scenario scenario;

   private DataReferenceField output;

   private DataReferenceField input;

   @Mock
   private PublishStepHandler publishStepHandler;

   @Mock
   private ReceiveStepHandler receiveStepHandler;

   @Before
   public void setup() throws Throwable {
      Data outputData = new Data("TestData1");
      Data inputData = new Data("TestData2");

      output = new DataReferenceField("myOutput");
      input = new DataReferenceField("myInput");
      output.setType(outputData);
      input.setType(inputData);

      Model model = new Model("TestModel");
      model.addOutput(output);
      model.addInput(input);

      scenario = new Scenario("testScenario");
      scenario.setParent(model);

      ScenarioStep receiveStep = new ScenarioStep();
      receiveStep.setKeyword(ReceiveStepHandler.PRESENT.getVerb());
      receiveStep.getParameters().add(input.getName());
      receiveStep.setParent(scenario);
      scenario.addWhen(receiveStep);

      ScenarioStep publishStep = new ScenarioStep();
      publishStep.setKeyword(PublishStepHandler.FUTURE.getVerb());
      publishStep.getParameters().add(output.getName());
      publishStep.setParent(scenario);
      scenario.addThen(publishStep);

      processor = new PubSubProcessor(publishStepHandler, receiveStepHandler);

      when(receiveStepHandler.getInputs(receiveStep)).thenReturn(input);
      when(publishStepHandler.getOutputs(publishStep)).thenReturn(output);
   }

   @Test
   public void testDoesComputeFlowPaths() throws Throwable {
      Collection<IPublishSubscribeMessagingFlow> flows = processor.getFlows(scenario);
      assertEquals("does not correct number of flows!",
                   1,
                   flows.size());

      IPublishSubscribeMessagingFlow flow = flows.iterator().next();
      assertEquals("flow type not correct!",
                   IPublishSubscribeMessagingFlow.FlowType.PATH,
                   flow.getFlowType());
      assertTrue("missing input!",
                 flow.getInputs().contains(input));
      assertTrue("missing output!",
                 flow.getOutputs().contains(output));
      assertEquals("scenario is not correct!",
                   scenario,
                   flow.getScenario());
   }

   @Test
   public void testDoesComputeFlowSources() throws Throwable {
      scenario.getWhens().clear();

      Collection<IPublishSubscribeMessagingFlow> flows = processor.getFlows(scenario);
      assertEquals("does not correct number of flows!",
                   1,
                   flows.size());

      IPublishSubscribeMessagingFlow flow = flows.iterator().next();
      assertEquals("flow type not correct!",
                   IPublishSubscribeMessagingFlow.FlowType.SOURCE,
                   flow.getFlowType());
      assertTrue("flow sources should not have input!",
                 flow.getInputs().isEmpty());
      assertTrue("missing output!",
                 flow.getOutputs().contains(output));
      assertEquals("scenario is not correct!",
                   scenario,
                   flow.getScenario());
   }

   @Test
   public void testDoesComputeFlowSinks() throws Throwable {
      scenario.getThens().clear();

      Collection<IPublishSubscribeMessagingFlow> flows = processor.getFlows(scenario);
      assertEquals("does not correct number of flows!",
                   1,
                   flows.size());

      IPublishSubscribeMessagingFlow flow = flows.iterator().next();
      assertEquals("flow type not correct!",
                   IPublishSubscribeMessagingFlow.FlowType.SINK,
                   flow.getFlowType());
      assertTrue("missing input!",
                 flow.getInputs().contains(input));
      assertTrue("flow sinks should not have output!",
                 flow.getOutputs().isEmpty());
      assertEquals("scenario is not correct!",
                   scenario,
                   flow.getScenario());
   }

   @Test
   public void testDoesComputeMessagingParadigms() throws Throwable {
      assertTrue("scenario should be pub/sub!",
                 processor.isPublishSubscribe(scenario));

      scenario.getWhens().clear();
      assertTrue("scenario should be pub/sub!",
                 processor.isPublishSubscribe(scenario));

      scenario.getThens().clear();
      assertFalse("scenario should not be pub/sub!",
                  processor.isPublishSubscribe(scenario));
   }
}
