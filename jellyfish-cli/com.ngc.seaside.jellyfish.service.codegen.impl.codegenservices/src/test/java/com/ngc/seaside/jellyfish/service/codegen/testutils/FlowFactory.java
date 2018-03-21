package com.ngc.seaside.jellyfish.service.codegen.testutils;

import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IRequestResponseMessagingFlow;
import com.ngc.seaside.systemdescriptor.model.impl.basic.Package;
import com.ngc.seaside.systemdescriptor.model.impl.basic.data.Data;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.DataReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.scenario.Scenario;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FlowFactory {

   private FlowFactory() {
   }

   public static IRequestResponseMessagingFlow newRequestResponseServerFlow(String scenarioName) {
      Scenario scenario = new Scenario(scenarioName);
      Package dataPackage = new Package("com.ngc.testing.data");

      Data rquestData = new Data("TestDataInput");
      Data responseData = new Data("TestDataOutput");
      rquestData.setParent(dataPackage);
      responseData.setParent(dataPackage);

      DataReferenceField requestRefField = new DataReferenceField("requestField");
      DataReferenceField responseRefField = new DataReferenceField("responseField");
      requestRefField.setType(rquestData);
      responseRefField.setType(responseData);

      IRequestResponseMessagingFlow flow = mock(IRequestResponseMessagingFlow.class);
      when(flow.getFlowType()).thenReturn(IRequestResponseMessagingFlow.FlowType.SERVER);
      when(flow.getScenario()).thenReturn(scenario);
      when(flow.getInput()).thenReturn(requestRefField);
      when(flow.getOutput()).thenReturn(responseRefField);
      return flow;
   }

   public static IPublishSubscribeMessagingFlow newPubSubFlowPath(String scenarioName) {
      Scenario scenario = new Scenario(scenarioName);
      Package dataPackage = new Package("com.ngc.testing.data");

      Data inputData = new Data("TestDataInput");
      Data outputData = new Data("TestDataOutput");
      inputData.setParent(dataPackage);
      outputData.setParent(dataPackage);

      DataReferenceField inputRefField = new DataReferenceField("inputField");
      DataReferenceField outputRefField = new DataReferenceField("outputField");
      inputRefField.setType(inputData);
      outputRefField.setType(outputData);

      IPublishSubscribeMessagingFlow flow = mock(IPublishSubscribeMessagingFlow.class);
      when(flow.getFlowType()).thenReturn(IPublishSubscribeMessagingFlow.FlowType.PATH);
      when(flow.getScenario()).thenReturn(scenario);
      when(flow.getInputs()).thenReturn(Collections.singletonList(inputRefField));
      when(flow.getOutputs()).thenReturn(Collections.singletonList(outputRefField));
      return flow;
   }

   public static IPublishSubscribeMessagingFlow newPubSubFlowSink(String scenarioName) {
      Scenario scenario = new Scenario(scenarioName);
      Package dataPackage = new Package("com.ngc.testing.data");

      Data inputData = new Data("TestDataInput");
      inputData.setParent(dataPackage);

      DataReferenceField inputRefField = new DataReferenceField("inputField");
      inputRefField.setType(inputData);

      IPublishSubscribeMessagingFlow flow = mock(IPublishSubscribeMessagingFlow.class);
      when(flow.getFlowType()).thenReturn(IPublishSubscribeMessagingFlow.FlowType.SINK);
      when(flow.getScenario()).thenReturn(scenario);
      when(flow.getInputs()).thenReturn(Collections.singletonList(inputRefField));
      return flow;
   }

   public static IPublishSubscribeMessagingFlow newPubSubFlowSource(String scenarioName) {
      Scenario scenario = new Scenario(scenarioName);
      Package dataPackage = new Package("com.ngc.testing.data");

      Data outputData = new Data("TestDataOutput");
      outputData.setParent(dataPackage);

      DataReferenceField outputRefField = new DataReferenceField("outputField");
      outputRefField.setType(outputData);

      IPublishSubscribeMessagingFlow flow = mock(IPublishSubscribeMessagingFlow.class);
      when(flow.getFlowType()).thenReturn(IPublishSubscribeMessagingFlow.FlowType.SOURCE);
      when(flow.getScenario()).thenReturn(scenario);
      when(flow.getOutputs()).thenReturn(Collections.singletonList(outputRefField));
      return flow;
   }
}
