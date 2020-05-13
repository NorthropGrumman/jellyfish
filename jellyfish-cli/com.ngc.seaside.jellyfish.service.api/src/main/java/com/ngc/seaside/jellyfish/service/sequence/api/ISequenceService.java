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
package com.ngc.seaside.jellyfish.service.sequence.api;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Collection;
import java.util.List;

/**
 * The sequence service generates {@link ISequence sequences} for different models.  This is useful to explore the
 * behavioral flow though a system.
 *
 * @see ISequence
 */
public interface ISequenceService {

   /**
    * The different strategies that can be used to generate the initial inputs to a sequence.
    */
   enum GenerationStrategy {
      /**
       * Indicates that all possible combinations of the original provided input should be used when generating inputs
       * for sequences.  For example, if the inputs {a, b, c} are initially provided, the following input sets will be
       * used to generate sequences.
       * <ul>
       * <li>{a}</li>
       * <li>{b}</li>
       * <li>{c}</li>
       * <li>{a, b}</li>
       * <li>{a, c}</li>
       * <li>{b, c}</li>
       * <li>{a, b, c}</li>
       * </ul>
       * Note this option can be dangerous if a large number of initial inputs are provided.  In particular, initial
       * inputs numbering 30 or more should be avoided since this can generate extremely large sets.
       */
      ALL_COMBINATIONS,

      /**
       * Indicates that only the provided inputs should be used when creating sequences.  For example, if the inputs
       * {a, b} are provided, only the input set {a, b} will be used to generate sequences.
       */
      EXACT,
   }

   /**
    * Generates sequences for the given model.
    *
    * @param options            the options Jellyfish was invoked with
    * @param model              the model to generate the sequences for
    * @param generationStrategy the strategy to use to generate the inputs to the sequence
    * @param inputFields        the inputs fields to use when generating the sets of inputs to the sequence (these must
    *                           be input fields declared in the model)
    * @return the generated sequences
    */
   List<ISequence> getSequences(IJellyFishCommandOptions options,
                                IModel model,
                                GenerationStrategy generationStrategy,
                                Collection<IDataReferenceField> inputFields);

   /**
    * Generates sequences for the given model using the model's declared input fields as the inputs.  The {@link
    * GenerationStrategy#ALL_COMBINATIONS} strategy will be used when generating inputs.
    *
    * @param options the options Jellyfish was invoked with
    * @param model   the model to generate the sequences for
    * @return the generated sequences
    */
   default List<ISequence> getSequences(IJellyFishCommandOptions options,
                                        IModel model) {
      if (model == null) {
         throw new NullPointerException("model may not be null!");
      }
      return getSequences(options, model, GenerationStrategy.ALL_COMBINATIONS, model.getInputs());
   }
}
