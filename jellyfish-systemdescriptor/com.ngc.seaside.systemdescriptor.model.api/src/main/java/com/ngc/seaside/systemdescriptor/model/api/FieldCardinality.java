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
package com.ngc.seaside.systemdescriptor.model.api;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

/**
 * Represents the cardinality of field declared within an {@link IModel} or {@link IData}.
 */
public enum FieldCardinality {
   /**
    * Indicates that a single field really represents multiple ports and may receive, transmit, or be associated with
    * more than one instance of the type declared by the field.
    */
   MANY,
   /**
    * The default value which indicates a single field is associated with exactly one instance of the type declared by
    * the field.
    */
   SINGLE
}
