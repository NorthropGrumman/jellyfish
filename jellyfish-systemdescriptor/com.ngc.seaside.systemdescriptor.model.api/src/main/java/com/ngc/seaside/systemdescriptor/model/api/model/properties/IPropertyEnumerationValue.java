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
package com.ngc.seaside.systemdescriptor.model.api.model.properties;

import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;

/**
 * Contains the value of a property when that value is a user defined enumeration type.
 */
public interface IPropertyEnumerationValue extends IPropertyValue {

   /**
    * Gets the enumeration type that this property is declared as.  The value of this property, if set, will be one of
    * the values contained in {@link IEnumeration#getValues()}.
    */
   IEnumeration getReferencedEnumeration();

   /**
    * Gets the value of this property.
    *
    * @return the value of this property
    * @throws IllegalStateException if this property is not {@link #isSet() set}
    */
   String getValue();

}
