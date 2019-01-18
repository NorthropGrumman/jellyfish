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
