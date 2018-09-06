/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.jellyfish.service.scenario.impl.scenarioservice.processor;

import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.correlation.api.ICorrelationDescription;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

public class PublishSubscribeMessagingFlow implements IPublishSubscribeMessagingFlow {

   private final Collection<IDataReferenceField> inputs = new ArrayList<>();

   private final Collection<IDataReferenceField> outputs = new ArrayList<>();

   private final FlowType flowType;

   private IScenario scenario;

   private ICorrelationDescription correlationDescription;

   public PublishSubscribeMessagingFlow(FlowType flowType) {
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

   public PublishSubscribeMessagingFlow setCorrelationDescriptor(ICorrelationDescription description) {
      this.correlationDescription = description;
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
      return Objects.equals(inputs, that.inputs)
            && Objects.equals(outputs, that.outputs)
            && flowType == that.flowType
            && Objects.equals(scenario, that.scenario);
   }

   @Override
   public int hashCode() {
      return Objects.hash(inputs,
                          outputs,
                          flowType,
                          scenario);
   }

   @Override
   public Optional<ICorrelationDescription> getCorrelationDescription() {
      return Optional.ofNullable(correlationDescription);
   }
}
