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
package com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Used to configure the {@link FlowGenerator}.
 */
public class FlowGeneratorContext {

   /**
    * The inputs that should be used to find flows.
    */
   private final Collection<IDataReferenceField> inputs = new ArrayList<>();

   /**
    * The starting sequence number of the flows.
    */
   private int sequenceNumber;

   /**
    * The model to investigate.
    */
   private IModel model;

   /**
    * The options used to run Jellyfish.
    */
   private IJellyFishCommandOptions options;

   /**
    * Gets the inputs to the generator.
    *
    * @return the inputs to the generator
    */
   public Collection<IDataReferenceField> getInputs() {
      return inputs;
   }

   /**
    * Sets the inputs to the generator.
    *
    * @param inputs the inputs to the generator
    * @return this context
    */
   public FlowGeneratorContext setInputs(Collection<? extends IDataReferenceField> inputs) {
      this.inputs.clear();
      this.inputs.addAll(inputs);
      return this;
   }

   /**
    * Gets the starting sequence number.
    *
    * @return the starting sequence number
    */
   public int getSequenceNumber() {
      return sequenceNumber;
   }

   /**
    * Sets the starting sequence number
    *
    * @param sequenceNumber the starting sequence number
    * @return this context
    */
   public FlowGeneratorContext setSequenceNumber(int sequenceNumber) {
      this.sequenceNumber = sequenceNumber;
      return this;
   }

   /**
    * Gets the model to explore.
    *
    * @return the model to explore
    */
   public IModel getModel() {
      return model;
   }

   /**
    * Sets the model to explore
    *
    * @param model the model to explore
    * @return this context
    */
   public FlowGeneratorContext setModel(IModel model) {
      this.model = model;
      return this;
   }

   /**
    * Gets options used to run Jellyfish.
    *
    * @return options used to run Jellyfish
    */
   public IJellyFishCommandOptions getOptions() {
      return options;
   }

   /**
    * sets options used to run Jellyfish.
    *
    * @param options options used to run Jellyfish
    * @return this context
    */
   public FlowGeneratorContext setOptions(IJellyFishCommandOptions options) {
      this.options = options;
      return this;
   }
}
