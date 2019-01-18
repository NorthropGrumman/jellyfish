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
package com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.model;

import com.google.common.base.Preconditions;

import com.ngc.seaside.jellyfish.service.sequence.api.ISequence;
import com.ngc.seaside.jellyfish.service.sequence.api.ISequenceFlow;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Implementation of {@code ISequence}.
 */
public class Sequence implements ISequence {

   /**
    * The flows of this sequence.
    */
   private final Collection<ISequenceFlow> flows = new ArrayList<>();

   /**
    * The ID of this sequence.
    */
   private final int id;

   /**
    * The model this sequence was generated for.
    */
   private IModel model;

   /**
    * The inputs to the sequence.
    */
   private final Collection<IDataReferenceField> inputs = new ArrayList<>();

   /**
    * The outputs to the sequence.
    */
   private final Collection<IDataReferenceField> outputs = new ArrayList<>();

   public Sequence(int id) {
      this.id = id;
   }

   @Override
   public int getId() {
      return id;
   }

   @Override
   public Collection<IDataReferenceField> getInputs() {
      return Collections.unmodifiableCollection(inputs);
   }

   /**
    * Clears any existing inputs and sets the inputs to the given values.
    *
    * @param inputs the new inputs
    * @return this sequence
    */
   public Sequence setInputs(Collection<IDataReferenceField> inputs) {
      Preconditions.checkNotNull(inputs, "inputs may not be null!");
      this.inputs.clear();
      this.inputs.addAll(inputs);
      return this;
   }

   /**
    * Adds an input to this sequence.
    *
    * @param input an input
    * @return this sequence
    */
   public Sequence addInput(IDataReferenceField input) {
      this.inputs.add(Preconditions.checkNotNull(input, "input may not be null!"));
      return this;
   }

   /**
    * Adds the given inputs to this sequence.
    *
    * @param inputs the inputs
    * @return this sequence
    */
   public Sequence addInputs(Collection<IDataReferenceField> inputs) {
      this.inputs.addAll(Preconditions.checkNotNull(inputs, "inputs may not be null!"));
      return this;
   }

   @Override
   public Collection<IDataReferenceField> getOutputs() {
      return Collections.unmodifiableCollection(outputs);
   }

   /**
    * Clears any existing outputs and sets the outputs to the given values.
    *
    * @param outputs the new outputs
    * @return this sequence
    */
   public Sequence setOutputs(Collection<IDataReferenceField> outputs) {
      Preconditions.checkNotNull(outputs, "outputs may not be null!");
      this.outputs.clear();
      this.outputs.addAll(outputs);
      return this;
   }

   /**
    * Adds an output to this sequence.
    *
    * @param output an output
    * @return this sequence
    */
   public Sequence addOutput(IDataReferenceField output) {
      this.outputs.add(Preconditions.checkNotNull(output, "output may not be null!"));
      return this;
   }

   /**
    * Adds the given outputs to this sequence.
    *
    * @param outputs the outputs
    * @return this sequence
    */
   public Sequence addOutputs(Collection<IDataReferenceField> outputs) {
      this.outputs.addAll(Preconditions.checkNotNull(outputs, "outputs may not be null!"));
      return this;
   }

   @Override
   public IModel getModel() {
      return model;
   }

   /**
    * Sets the model for this sequence.
    *
    * @param model the model
    * @return this sequence
    */
   public Sequence setModel(IModel model) {
      this.model = model;
      return this;
   }

   @Override
   public Collection<ISequenceFlow> getFlows() {
      return Collections.unmodifiableCollection(flows);
   }

   /**
    * Clears any existing flows and sets the flows to the given values.
    *
    * @param flows the new flows
    * @return this sequence
    */
   public Sequence setFlows(Collection<? extends ISequenceFlow> flows) {
      Preconditions.checkNotNull(flows, "flows may not be null!");
      this.flows.clear();
      this.flows.addAll(flows);
      return this;
   }

   /**
    * Adds the given flow.
    *
    * @param flow the flow to add
    * @return this sequence
    */
   public Sequence addFlow(ISequenceFlow flow) {
      this.flows.add(Preconditions.checkNotNull(flow, "flow may not be null!"));
      return this;
   }

   /**
    * Adds the given flows.
    *
    * @param flows the flow to add
    * @return this sequence
    */
   public Sequence addFlows(Collection<? extends ISequenceFlow> flows) {
      this.flows.addAll(Preconditions.checkNotNull(flows, "flows may not be null!"));
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof Sequence)) {
         return false;
      }
      Sequence sequence = (Sequence) o;
      return id == sequence.id
             && Objects.equals(model, sequence.model);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, model);
   }
}
