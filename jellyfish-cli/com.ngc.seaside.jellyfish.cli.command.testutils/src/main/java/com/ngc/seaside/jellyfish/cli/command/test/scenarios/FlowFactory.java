package com.ngc.seaside.jellyfish.cli.command.test.scenarios;

import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IRequestResponseMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.correlation.api.ICorrelationDescription;
import com.ngc.seaside.jellyfish.service.scenario.correlation.api.ICorrelationExpression;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataPath;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.impl.basic.Package;
import com.ngc.seaside.systemdescriptor.model.impl.basic.data.Data;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.DataReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.scenario.Scenario;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FlowFactory {

   private FlowFactory() {
   }


   /**
    * Creates a mocked {@link IRequestResponseMessagingFlow} containing a scenario with the given
    * scenario name
    *
    * @param scenarioName the scenario name
    * @return a mocked IRequestResponseMessagingFlow
    */
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
      when(flow.getCorrelationDescription()).thenReturn(Optional.empty());
      return flow;
   }

   /**
    * Creates a mocked {@link IRequestResponseMessagingFlow} containing the given scenario
    *
    * @param scenario the scenario
    * @return a mocked IRequestResponseMessagingFlow
    */
   public static IRequestResponseMessagingFlow newRequestResponseServerFlow(IScenario scenario) {
      IModel model = scenario.getParent();

      IRequestResponseMessagingFlow flow = mock(IRequestResponseMessagingFlow.class);
      IDataReferenceField input = model.getInputs().iterator().next();
      IDataReferenceField output = model.getOutputs().iterator().next();

      when(flow.getScenario()).thenReturn(scenario);
      when(flow.getFlowType()).thenReturn(IRequestResponseMessagingFlow.FlowType.SERVER);
      when(flow.getInput()).thenReturn(input);
      when(flow.getOutput()).thenReturn(output);
      when(flow.getCorrelationDescription()).thenReturn(Optional.empty());

      return flow;
   }

   /**
    * Creates a mocked {@link IRequestResponseMessagingFlow} containing the given scenario and input/output fields.
    *
    * @param scenario        the scenario
    * @param inputFieldName  the input field name
    * @param outputFieldName the output field name
    * @return a mocked IRequestResponseMessagingFlow
    */
   public static IRequestResponseMessagingFlow newRequestResponseServerFlow(IScenario scenario,
                                                                            String inputFieldName,
                                                                            String outputFieldName) {
      IModel model = scenario.getParent();

      IRequestResponseMessagingFlow flow = mock(IRequestResponseMessagingFlow.class);
      IDataReferenceField input = model.getInputs().getByName(inputFieldName).get();
      IDataReferenceField output = model.getOutputs().getByName(outputFieldName).get();

      when(flow.getScenario()).thenReturn(scenario);
      when(flow.getFlowType()).thenReturn(IRequestResponseMessagingFlow.FlowType.SERVER);
      when(flow.getInput()).thenReturn(input);
      when(flow.getOutput()).thenReturn(output);
      when(flow.getCorrelationDescription()).thenReturn(Optional.empty());

      return flow;
   }

   /**
    * Creates a mocked {@link IPublishSubscribeMessagingFlow} containing a scenario with the given
    * scenario name
    *
    * @param scenarioName the scenario name
    * @return a mocked IPublishSubscribeMessagingFlow
    */
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
      when(flow.getCorrelationDescription()).thenReturn(Optional.empty());
      return flow;
   }

   /**
    * Creates a mocked {@link IPublishSubscribeMessagingFlow} containing the given scenario
    *
    * @param scenario the scenario
    * @return a mocked IPublishSubscribeMessagingFlow
    */
   public static IPublishSubscribeMessagingFlow newPubSubFlowPath(IScenario scenario) {
      IModel model = scenario.getParent();

      IPublishSubscribeMessagingFlow flow = mock(IPublishSubscribeMessagingFlow.class);
      IDataReferenceField input = model.getInputs().iterator().next();
      IDataReferenceField output = model.getOutputs().iterator().next();

      when(flow.getScenario()).thenReturn(scenario);
      when(flow.getInputs()).thenReturn(Collections.singleton(input));
      when(flow.getOutputs()).thenReturn(Collections.singleton(output));
      when(flow.getCorrelationDescription()).thenReturn(Optional.empty());

      return flow;
   }

   /**
    * Creates a mocked {@link IPublishSubscribeMessagingFlow} that supports correlation containing the given scenario.
    *
    * @param scenario the scenario
    * @return a mocked IPublishSubscribeMessagingFlow
    */
   public static IPublishSubscribeMessagingFlow newCorrelatingPubSubFlowPath(IScenario scenario) {
      IModel model = scenario.getParent();

      IPublishSubscribeMessagingFlow flow = mock(IPublishSubscribeMessagingFlow.class);
      IDataReferenceField input = model.getInputs().iterator().next();
      IDataReferenceField output = model.getOutputs().iterator().next();

      when(flow.getScenario()).thenReturn(scenario);
      when(flow.getInputs()).thenReturn(Collections.singleton(input));
      when(flow.getOutputs()).thenReturn(Collections.singleton(output));

      IDataField field1 = mock(IDataField.class);
      IDataField field2 = mock(IDataField.class);
      IDataPath left = mock(IDataPath.class);
      IDataPath right = mock(IDataPath.class);
      ICorrelationExpression expression = mock(ICorrelationExpression.class);
      ICorrelationDescription correlation = mock(ICorrelationDescription.class);

      when(left.getStart()).thenReturn(input);
      when(right.getStart()).thenReturn(input);
      when(left.getElements()).thenReturn(Arrays.asList(field1, field2));
      when(right.getElements()).thenReturn(Arrays.asList(field1, field2));
      when(expression.getLeftHandOperand()).thenReturn(left);
      when(expression.getRightHandOperand()).thenReturn(right);
      when(expression.getCorrelationEventIdType()).thenReturn(DataTypes.STRING);
      when(correlation.getCorrelationExpressions()).thenReturn(Collections.singleton(expression));
      when(correlation.getCompletenessExpressions()).thenReturn(Collections.singleton(expression));
      when(flow.getCorrelationDescription()).thenReturn(Optional.of(correlation));

      return flow;
   }

   /**
    * Creates a mocked {@link IPublishSubscribeMessagingFlow} containing a scenario with the given
    * scenario name.  Flow type is sink.
    *
    * @param scenarioName the scenario name
    * @return a mocked IPublishSubscribeMessagingFlow
    */
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
      when(flow.getCorrelationDescription()).thenReturn(Optional.empty());
      return flow;
   }

   /**
    * Creates a mocked {@link IPublishSubscribeMessagingFlow} containing a scenario with the given
    * scenario name.  Flow type is source.
    *
    * @param scenarioName the scenario name
    * @return a mocked IPublishSubscribeMessagingFlow
    */
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
      when(flow.getCorrelationDescription()).thenReturn(Optional.empty());
      return flow;
   }
}
