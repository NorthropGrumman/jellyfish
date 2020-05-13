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
package com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.model;

import com.ngc.seaside.jellyfish.service.scenario.api.IMessagingFlow;
import com.ngc.seaside.jellyfish.service.sequence.api.ISequenceFlow;
import com.ngc.seaside.jellyfish.service.sequence.api.ISequenceFlowImplementation;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of {@code ISequenceFlow}.
 */
public class SequenceFlow implements ISequenceFlow {

   /**
    * The inputs to this flow.
    */
   private final Collection<IDataReferenceField> inputs = new ArrayList<>();

   /**
    * The outputs of this flow.
    */
   private final Collection<IDataReferenceField> outputs = new ArrayList<>();

   /**
    * The messaging flow being wrapped.
    */
   private final IMessagingFlow messagingFlow;

   /**
    * The sequence number of this flow.
    */
   private int sequenceNumber = -1;

   /**
    * The implementation of this flow (may be {@code null}).
    */
   private SequenceFlowImplementation implementation;

   /**
    * Creates a new flow that wraps the given messaging flow
    *
    * @param messagingFlow the messaging flow to wrap.
    */
   public SequenceFlow(IMessagingFlow messagingFlow) {
      this.messagingFlow = messagingFlow;
   }

   @Override
   public IMessagingFlow getMessagingFlow() {
      return messagingFlow;
   }

   @Override
   public int getSequenceNumber() {
      return sequenceNumber;
   }

   /**
    * Sets the sequence number of this flow.
    *
    * @param sequenceNumber sequence number of this flow
    * @return this flow
    */
   public SequenceFlow setSequenceNumber(int sequenceNumber) {
      this.sequenceNumber = sequenceNumber;
      return this;
   }

   @Override
   public Collection<IDataReferenceField> getInputs() {
      return Collections.unmodifiableCollection(inputs);
   }

   /**
    * Adds the given inputs to this flow.
    *
    * @param inputs the inputs to this flow
    * @return this flow
    */
   public SequenceFlow addInputs(Collection<IDataReferenceField> inputs) {
      this.inputs.addAll(inputs);
      return this;
   }

   @Override
   public Collection<IDataReferenceField> getOutputs() {
      return Collections.unmodifiableCollection(outputs);
   }

   /**
    * Adds the given outputs to this flow.
    *
    * @param outputs the outputs to this flow
    * @return this flow
    */
   public SequenceFlow addOutputs(Collection<IDataReferenceField> outputs) {
      this.outputs.addAll(outputs);
      return this;
   }

   @Override
   public Optional<ISequenceFlowImplementation> getImplementation() {
      return Optional.ofNullable(implementation);
   }

   /**
    * Sets the implementation of this flow.
    *
    * @param implementation the implementation
    * @return this flow
    */
   public SequenceFlow setImplementation(SequenceFlowImplementation implementation) {
      this.implementation = implementation;
      return this;
   }

   /**
    * Returns true if this flow is <i>logically</i> equivalent to the given flow.
    *
    * @param that the flow to compare
    * @return true if this flow is logically equivalent to the given flow
    */
   public boolean isEquivalent(SequenceFlow that) {
      // We use this because equals doesn't work on the collection types the way we want it to.
      return areEquivalent(this, that);
   }

   @Override
   public String toString() {
      return String.format("[Sequence flow for %s.%s with inputs %s and outputs %s]",
                           messagingFlow.getScenario().getParent().getFullyQualifiedName(),
                           messagingFlow.getScenario().getName(),
                           getInputs().stream().map(INamedChild::getName).collect(Collectors.joining(", ")),
                           getOutputs().stream().map(INamedChild::getName).collect(Collectors.joining(", ")));
   }

   /**
    * Returns true if the two flows are <i>logically</i> equivalent.
    *
    * @param left  the first flow to compare
    * @param right the second flow to compare
    * @return true if the two flows are <i>logically</i> equivalent
    */
   static boolean areEquivalent(SequenceFlow left, SequenceFlow right) {
      if (left == right) {
         return true;
      }
      if (left == null || right == null) {
         return false;
      }
      return left.sequenceNumber == right.sequenceNumber
             && Objects.equals(left.inputs, right.inputs)
             && Objects.equals(left.outputs, right.outputs)
             && Objects.equals(left.messagingFlow, right.messagingFlow)
             && SequenceFlowImplementation.areEquivalent(left.implementation, right.implementation);
   }
}
