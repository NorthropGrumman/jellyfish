/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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

import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.model.Sequence;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Used to configure the {@link SequenceGenerator}.
 */
public class SequenceGeneratorContext {

   /**
    * The inputs consumed by the sequence.
    */
   private final Collection<IDataReferenceField> consumedInputs = new ArrayList<>();

   /**
    * The inputs to use to generate the sequence.
    */
   private final Collection<IDataReferenceField> inputs = new ArrayList<>();

   /**
    * The sequence that was generated.
    */
   private Sequence sequence;

   /**
    * Gets the inputs consumed by the sequence.
    *
    * @return the inputs consumed by the sequence
    */
   public Collection<IDataReferenceField> getConsumedInputs() {
      return consumedInputs;
   }

   /**
    * Gets the inputs to use to generate the sequence.
    *
    * @return the inputs to use to generate the sequence
    */
   public Collection<IDataReferenceField> getInputs() {
      return inputs;
   }

   /**
    * Sets the inputs to use to generate the sequence.
    *
    * @param inputs the inputs to use to generate the sequence.
    * @return this context
    */
   public SequenceGeneratorContext setInputs(Collection<? extends IDataReferenceField> inputs) {
      this.inputs.clear();
      this.inputs.addAll(inputs);
      return this;
   }

   /**
    * The generated sequence.
    *
    * @return the generate sequence
    */
   public Sequence getSequence() {
      return sequence;
   }

   /**
    * Sets the generated sequence.
    *
    * @param sequence the generated sequence
    * @return this context
    */
   public SequenceGeneratorContext setSequence(Sequence sequence) {
      this.sequence = sequence;
      return this;
   }
}
