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

import com.google.common.base.Preconditions;

import com.ngc.seaside.jellyfish.service.sequence.api.ISequenceFlow;
import com.ngc.seaside.jellyfish.service.sequence.api.ISequenceFlowImplementation;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.Sequencing;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of {@code ISequenceFlowImplementation}.
 */
public class SequenceFlowImplementation implements ISequenceFlowImplementation {

   /**
    * The nested flows that make up this implementation.
    */
   private final List<SequenceFlow> flows = new ArrayList<>();

   /**
    * The incoming links to this flow.  Mapped first by nested flow and then by the input field associated
    * with the link's source.
    */
   private final Map<ISequenceFlow, Map<IDataReferenceField, IModelLink<IDataReferenceField>>> incomingLinks =
         new LinkedHashMap<>();

   /**
    * The outgoing links of this flow.  Mapped first by nested flow and then by the output field associated
    * with the link's target.
    */
   private final Map<ISequenceFlow, Map<IDataReferenceField, IModelLink<IDataReferenceField>>> outgoingLinks =
         new LinkedHashMap<>();

   /**
    * The components that are implementing the nested flows.
    */
   private final Map<ISequenceFlow, IModelReferenceField> componentsImplementingFlows = new HashMap<>();

   /**
    * Points back to the parent flow this implementation is for.
    */
   private ISequenceFlow parentFlow;

   @Override
   public ISequenceFlow getParentFlow() {
      return parentFlow;
   }

   /**
    * Sets the parent flow of this implementation.
    *
    * @param parentFlow the parent flow of this implementation
    * @return this implementation
    */
   public SequenceFlowImplementation setParentFlow(ISequenceFlow parentFlow) {
      this.parentFlow = parentFlow;
      return this;
   }

   @Override
   public Collection<ISequenceFlow> getFlows() {
      return Collections.unmodifiableCollection(flows);
   }

   /**
    * Adds the given flow to this implementation.
    *
    * @param flow the flow to add
    * @return this implementation
    */
   public SequenceFlowImplementation addFlow(SequenceFlow flow) {
      this.flows.add(flow);
      // Sort the flows by sequence number.
      this.flows.sort(Comparator.comparingInt(ISequenceFlow::getSequenceNumber));
      return this;
   }

   @Override
   public Collection<IModelLink<IDataReferenceField>> getIncomingLinks() {
      // We only want the incoming links of the flows which are in the first sequence.
      return Collections.unmodifiableCollection(
            flows.stream()
                  .filter(f -> f.getSequenceNumber() == 0)
                  .flatMap(f -> getIncomingLinks(f).values().stream())
                  .collect(Collectors.toList()));
   }

   @Override
   public Map<IDataReferenceField, IModelLink<IDataReferenceField>> getIncomingLinks(ISequenceFlow flow) {
      Preconditions.checkNotNull(flow, "flow may not be null!");
      Preconditions.checkArgument(flows.contains(flow), "flow is not part of this implementation: %s!", flow);
      return Collections.unmodifiableMap(incomingLinks.getOrDefault(flow, Collections.emptyMap()));
   }

   @Override
   public IModelLink<IDataReferenceField> getIncomingLink(ISequenceFlow flow, IDataReferenceField input) {
      Preconditions.checkNotNull(input, "input may not be null!");
      IModelLink<IDataReferenceField> link = getIncomingLinks(flow).get(input);
      Preconditions.checkArgument(link != null, "no incoming link for flow %s with a target of %s!", flow, input);
      return link;
   }

   /**
    * Adds the incoming link to this implementation.
    *
    * @param flow the nested flow which is on the target end of the link
    * @param link the link whose source will eventually reach the given nested flow
    * @return this implementation
    */
   public SequenceFlowImplementation addIncomingLink(ISequenceFlow flow, IModelLink<IDataReferenceField> link) {
      incomingLinks.computeIfAbsent(flow, f -> new LinkedHashMap<>()).put(link.getTarget(), link);
      return this;
   }

   @Override
   public Collection<IModelLink<IDataReferenceField>> getOutgoingLinks() {
      // We only want the links for the last flows.
      int maxSequenceNumber = flows.stream().mapToInt(ISequenceFlow::getSequenceNumber).max().orElse(-1);
      return Collections.unmodifiableCollection(
            flows.stream()
                  .filter(f -> f.getSequenceNumber() == maxSequenceNumber)
                  .flatMap(f -> getOutgoingLinks(f).values().stream())
                  .collect(Collectors.toList()));
   }

   @Override
   public Map<IDataReferenceField, IModelLink<IDataReferenceField>> getOutgoingLinks(ISequenceFlow flow) {
      Preconditions.checkNotNull(flow, "flow may not be null!");
      Preconditions.checkArgument(flows.contains(flow), "flow is not part of this implementation: %s!", flow);
      return Collections.unmodifiableMap(outgoingLinks.getOrDefault(flow, Collections.emptyMap()));
   }

   @Override
   public IModelLink<IDataReferenceField> getOutgoingLink(ISequenceFlow flow, IDataReferenceField output) {
      Preconditions.checkNotNull(output, "input may not be null!");
      IModelLink<IDataReferenceField> link = getOutgoingLinks(flow).get(output);
      Preconditions.checkArgument(link != null, "no outgoing link for flow %s with a source of %s!", flow, output);
      return link;
   }

   /**
    * Adds the outgoing link to this implementation.
    *
    * @param flow the nested flow which is on the source end of the link
    * @param link the link whose target will eventually leave the given nested flow
    * @return this implementation
    */
   public SequenceFlowImplementation addOutgoingLink(ISequenceFlow flow, IModelLink<IDataReferenceField> link) {
      outgoingLinks.computeIfAbsent(flow, f -> new LinkedHashMap<>()).put(link.getSource(), link);
      return this;
   }

   @Override
   public IModelReferenceField getComponentImplementingFlow(ISequenceFlow flow) {
      Preconditions.checkNotNull(flow, "flow may not be null!");
      Preconditions.checkArgument(flows.contains(flow), "flow is not part of this implementation: %s!", flow);
      IModelReferenceField component = componentsImplementingFlows.get(flow);
      Preconditions.checkArgument(component != null, "no components implements flow %s!", flow);
      return component;
   }

   /**
    * Adds a component which implements the given nested flow.
    *
    * @param flow      the nested flow
    * @param component the component that implements the nested flow
    * @return this implementation
    */
   public SequenceFlowImplementation addComponentImplementingFlow(ISequenceFlow flow, IModelReferenceField component) {
      componentsImplementingFlows.put(flow, component);
      return this;
   }

   /**
    * Returns true if this implementation is <i>logically</i> equivalent to the given implementation.
    *
    * @param that the implementation to compare
    * @return true if this implementation is logically equivalent to the given implementation
    */
   public boolean isEquivalent(SequenceFlowImplementation that) {
      return areEquivalent(this, that);
   }

   /**
    * Returns true if the two implementations are <i>logically</i> equivalent.
    *
    * @param left  the first implementation to compare
    * @param right the second implementation to compare
    * @return true if the two implementations are <i>logically</i> equivalent
    */
   static boolean areEquivalent(SequenceFlowImplementation left, SequenceFlowImplementation right) {
      if (left == right) {
         return true;
      }
      if (left == null || right == null) {
         return false;
      }
      return Sequencing.equivalent(left.flows, right.flows, SequenceFlow::areEquivalent)
             && Sequencing.equivalent(left.incomingLinks,
                                      right.incomingLinks,
                                      (a, b) -> SequenceFlow.areEquivalent((SequenceFlow) a, (SequenceFlow) b),
                                      Map::equals)
             && Sequencing.equivalent(left.outgoingLinks,
                                      right.outgoingLinks,
                                      (a, b) -> SequenceFlow.areEquivalent((SequenceFlow) a, (SequenceFlow) b),
                                      Map::equals)
             && Sequencing.equivalent(left.componentsImplementingFlows,
                                      right.componentsImplementingFlows,
                                      (a, b) -> SequenceFlow.areEquivalent((SequenceFlow) a, (SequenceFlow) b),
                                      IModelReferenceField::equals);
   }
}
