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

   public Collection<IDataReferenceField> getInputsModifiable() {
      return inputs;
   }

   @Override
   public Collection<IDataReferenceField> getOutputs() {
      return Collections.unmodifiableCollection(outputs);
   }

   public Collection<IDataReferenceField> getOutputsModifiable() {
      return outputs;
   }

   @Override
   public FlowType getFlowType() {
      return flowType;
   }

   @Override
   public IScenario getScenario() {
      return scenario;
   }

   public PublishSubscribeMessagingFlow setScenario(IScenario scenario) {
      this.scenario = scenario;
      return this;
   }

   @Override
   public Optional<ICorrelationDescription> getCorrelationDescription() {
      return Optional.ofNullable(correlationDescription);
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
}
