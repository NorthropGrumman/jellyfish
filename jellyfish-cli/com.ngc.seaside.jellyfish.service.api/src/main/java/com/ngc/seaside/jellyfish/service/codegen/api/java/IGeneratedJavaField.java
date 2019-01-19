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
package com.ngc.seaside.jellyfish.service.codegen.api.java;

import com.ngc.seaside.jellyfish.service.codegen.api.IGeneratedField;

public interface IGeneratedJavaField extends IGeneratedField {

   /**
    * Returns the fully qualified name of the java type of this field. If this field {@link #isMultiple()},
    * returns the java type of the elements.
    *
    * @return the fully qualified name of the java type of this field
    */
   String getJavaType();

   /**
    * @return the name of the java field
    */
   String getJavaFieldName();

   /**
    * @return the name of the getter method for this java field
    */
   String getJavaGetterName();

   /**
    * @return the name of the setter method for this java field
    */
   String getJavaSetterName();

}
