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
