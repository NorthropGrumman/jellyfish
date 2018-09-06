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

import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;

import java.util.Optional;

/**
 * Represents a field declared in an {@link IModel} that references another model.  Operations that change the state of
 * this object may throw {@code UnsupportedOperationException}s if the object is immutable.
 */
public interface IModelReferenceField extends IReferenceField {

   /**
    * Gets the model type of this field that is being referenced.
    *
    * @return the model type of this field that is being referenced
    */
   IModel getType();

   /**
    * Sets the model type of this field that is being referenced
    *
    * @param model the model type of this field that is being referenced
    * @return this field
    */
   IModelReferenceField setType(IModel model);

   /**
    * Gets the field that this field refines. If this field does not refine a field, the optional is empty.
    *
    * @return the field that this field refines
    */
   Optional<IModelReferenceField> getRefinedField();

   /**
    * Gets the properties of this field.
    *
    * @return the properties of this field (never {@code null})
    */
   IProperties getProperties();

   /**
    * Sets the properties of this field.
    *
    * @param properties the properties of this field
    * @return this field
    */
   IReferenceField setProperties(IProperties properties);
}
