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

import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;

/**
 * A reference field is a field declared within an {@link IModel} that may reference either another {@code IModel} or
 * {@link com.ngc.seaside.systemdescriptor.model.api.data.IData} type.  Operations that change the state of this object
 * may throw {@code UnsupportedOperationException}s if the object is immutable.
 */
public interface IReferenceField extends INamedChild<IModel> {

   /**
    * Gets the metadata associated with this field.
    *
    * @return the metadata associated with this field
    */
   IMetadata getMetadata();

   /**
    * Sets the metadata associated with this field.
    *
    * @param metadata the metadata associated with this field
    * @return this field
    */
   IReferenceField setMetadata(IMetadata metadata);

}
