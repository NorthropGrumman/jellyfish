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

import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Collection;

/**
 * A sequence is a container object for {@link ISequenceFlow}s.  A sequence is generated from a single {@link IModel}
 * and it must contain at least one flow.  A sequence must either consume at least one input or generate at least one
 * output.
 */
public interface ISequence {

   /**
    * Gets the ID of this sequence.  Sequence IDs are unique for the same {@link #getModel() model}.
    *
    * @return the ID of this sequence
    */
   int getId();

   /**
    * Gets the inputs that this sequence consumes.  These are the inputs that trigger the sequence.  These inputs will
    * be consumed by one or more {@link ISequenceFlow flows} contained in this sequence.
    *
    * @return the inputs of the sequence
    */
   Collection<IDataReferenceField> getInputs();

   /**
    * Gets the outputs that this sequence generates.  This is the aggregate output of all the flows contained by this
    * sequence.
    *
    * @return the outputs of this sequence
    */
   Collection<IDataReferenceField> getOutputs();

   /**
    * Gets the model this sequence is for.
    *
    * @return the model that contains this sequence
    */
   IModel getModel();

   /**
    * Gets the flows that this sequence contains.  The returned flows are ordered by {@link
    * ISequenceFlow#getSequenceNumber sequence number}.
    */
   Collection<ISequenceFlow> getFlows();
}
