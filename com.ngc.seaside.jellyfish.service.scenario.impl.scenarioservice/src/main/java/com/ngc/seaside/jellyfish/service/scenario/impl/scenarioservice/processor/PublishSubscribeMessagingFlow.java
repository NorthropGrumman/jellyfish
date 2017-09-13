package com.ngc.seaside.jellyfish.service.scenario.impl.scenarioservice.processor;

import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class PublishSubscribeMessagingFlow implements IPublishSubscribeMessagingFlow {

   private final Collection<IDataReferenceField> inputs = new ArrayList<>();

   private final Collection<IDataReferenceField> outputs = new ArrayList<>();

   private final FlowType flowType;

   private IScenario scenario;

   public PublishSubscribeMessagingFlow(
         FlowType flowType) {
      this.flowType = flowType;
   }

   @Override
   public Collection<IDataReferenceField> getInputs() {
      return Collections.unmodifiableCollection(inputs);
   }

   @Override
   public Collection<IDataReferenceField> getOutputs() {
      return Collections.unmodifiableCollection(outputs);
   }

   @Override
   public FlowType getFlowType() {
      return flowType;
   }

   @Override
   public IScenario getScenario() {
      return scenario;
   }

   public Collection<IDataReferenceField> getInputsModifiable() {
      return inputs;
   }

   public Collection<IDataReferenceField> getOutputsModifiable() {
      return outputs;
   }

   public PublishSubscribeMessagingFlow setScenario(IScenario scenario) {
      this.scenario = scenario;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof PublishSubscribeMessagingFlow)) {
         return false;
      }
      PublishSubscribeMessagingFlow that = (PublishSubscribeMessagingFlow) o;
      return Objects.equals(inputs, that.inputs) &&
             Objects.equals(outputs, that.outputs) &&
             flowType == that.flowType &&
             Objects.equals(scenario, that.scenario);
   }

   @Override
   public int hashCode() {
      return Objects.hash(inputs,
                          outputs,
                          flowType,
                          scenario);
   }
}
