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
