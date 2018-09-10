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
package com.ngc.seaside.jellyfish.service.codegen.api.dto;

/**
 * Interface representing a Java type.
 * 
 * @param <T> class implementing this interface
 */
public interface TypeDto<T> {

   /**
    * Gets the unqualified type name.
    */
   public String getTypeName();

   public T setTypeName(String packageName);

   /**
    * Gets the package of the type.
    */
   public String getPackageName();

   public T setPackageName(String packageName);

   default String getFullyQualifiedName() {
      return getPackageName() + "." + getTypeName();
   }
   
}
