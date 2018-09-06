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
package com.ngc.seaside.systemdescriptor.model.api.model;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;

/**
 * Represents a field declared in an {@link IModel} that references another {@link IData} type.  Operations that change
 * the state of this object may throw {@code UnsupportedOperationException}s if the object is immutable.
 */
public interface IDataReferenceField extends IReferenceField {

   /**
    * Gets the model type of this field that is being referenced
    *
    * @return the model type of this field that is being referenced
    */
   IData getType();

   /**
    * Sets the data type of this field that is being referenced.
    *
    * @param type the data type of this field that is being referenced
    * @return this field
    */
   IDataReferenceField setType(IData type);

   /**
    * Gets the cardinality associated with this field.
    *
    * @return the cardinality associated with this field
    */
   FieldCardinality getCardinality();

   /**
    * Sets the cardinality associated with this field.
    *
    * @param cardinality the cardinality associated with this field
    * @return this field
    */
   IDataReferenceField setCardinality(FieldCardinality cardinality);
}
