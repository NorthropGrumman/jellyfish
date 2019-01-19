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
package com.ngc.seaside.jellyfish.service.codegen.api.proto;

import com.ngc.seaside.jellyfish.service.codegen.api.java.IGeneratedJavaField;

public interface IGeneratedJavaProtoField extends IGeneratedJavaField {

   /**
    * Returns the name of the count method for a repeated field.
    * 
    * @return the name of the count method for a repeated field
    * @throws RuntimeException if {@link #isMultiple()} is false for this field
    */
   String getRepeatedJavaCountName();

   /**
    * Returns the name of the add method for a repeated field.
    * 
    * @return the name of the add method for a repeated field
    * @throws RuntimeException if {@link #isMultiple()} is false for this field
    */
   String getRepeatedJavaAddName();

   /**
    * Returns the name of the get-at-index method for a repeated field.
    * @return the name of the get-at-index method for a repeated field
    * @throws RuntimeException if {@link #isMultiple()} is false for this field
    */
   String getRepeatedJavaGetterName();

}
