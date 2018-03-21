package com.ngc.seaside.jellyfish.service.scenario.impl.scenarioservice.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow;
import com.ngc.seaside.systemdescriptor.model.impl.basic.data.Data;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.DataReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.Model;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.scenario.Scenario;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.scenario.ScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.CorrelateStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.PublishStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.ReceiveStepHandler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

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
   
   @Mock
   private CorrelateStepHandler correlateStepHandler;

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

      when(receiveStepHandler.getInputs(receiveStep)).thenReturn(input);
      when(publishStepHandler.getOutputs(publishStep)).thenReturn(output);

      processor = new PubSubProcessor(publishStepHandler, receiveStepHandler, correlateStepHandler);
   }

   @Test
   public void testDoesComputeFlowPaths() {
      Optional<IPublishSubscribeMessagingFlow> optionalFlow = processor.getFlow(scenario);
      assertTrue("expected a flow!", optionalFlow.isPresent());

      IPublishSubscribeMessagingFlow flow = optionalFlow.get();
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
   public void testDoesComputeFlowSources() {
      scenario.getWhens().clear();

      Optional<IPublishSubscribeMessagingFlow> optionalFlow = processor.getFlow(scenario);
      assertTrue("expected a flow!", optionalFlow.isPresent());

      IPublishSubscribeMessagingFlow flow = optionalFlow.get();
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
   public void testDoesComputeFlowSinks() {
      scenario.getThens().clear();

      Optional<IPublishSubscribeMessagingFlow> optionalFlow = processor.getFlow(scenario);
      assertTrue("expected a flow!", optionalFlow.isPresent());

      IPublishSubscribeMessagingFlow flow = optionalFlow.get();
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
   public void testDoesComputeMessagingParadigms() {
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
