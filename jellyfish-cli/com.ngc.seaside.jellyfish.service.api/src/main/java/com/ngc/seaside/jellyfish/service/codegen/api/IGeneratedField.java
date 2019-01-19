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
package com.ngc.seaside.jellyfish.service.codegen.api;

import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;

/**
 * Common interface for generating fields from an {@link IDataField}.
 */
public interface IGeneratedField {

   /**
    * @return the {@link IDataField} on which this generated field is based
    */
   IDataField getDataField();

   /**
    * @return true if this type represents a collection of elements
    */
   boolean isMultiple();

}
