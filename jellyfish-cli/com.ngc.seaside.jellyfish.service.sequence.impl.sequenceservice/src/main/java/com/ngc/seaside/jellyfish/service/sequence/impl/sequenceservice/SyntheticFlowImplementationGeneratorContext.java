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
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.model.SequenceFlow;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Used to configure the {@link SyntheticFlowImplementationGenerator}.
 */
public class SyntheticFlowImplementationGeneratorContext {

   /**
    * The inputs that should be used to find flows.
    */
   private Collection<IDataReferenceField> inputs;

   /**
    * Flows that have been declared.
    */
   private Collection<SequenceFlow> declaredFlows = new ArrayList<>();

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
   public SyntheticFlowImplementationGeneratorContext setInputs(Collection<IDataReferenceField> inputs) {
      this.inputs = inputs;
      return this;
   }

   /**
    * Gets the flows that have been declared.
    *
    * @return the flows that have been declared
    */
   public Collection<SequenceFlow> getDeclaredFlows() {
      return declaredFlows;
   }

   /**
    * Sets the flows that have been declared.
    *
    * @param declaredFlows the flows that have been declared
    * @return this context
    */
   public SyntheticFlowImplementationGeneratorContext setDeclaredFlows(Collection<SequenceFlow> declaredFlows) {
      this.declaredFlows = declaredFlows;
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
   public SyntheticFlowImplementationGeneratorContext setModel(IModel model) {
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
   public SyntheticFlowImplementationGeneratorContext setOptions(IJellyFishCommandOptions options) {
      this.options = options;
      return this;
   }
}
