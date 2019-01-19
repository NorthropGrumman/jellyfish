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
package com.ngc.seaside.jellyfish.api;

/**
 * An input parameter to a command.
 */
public interface IParameter<T> {

   /**
    * Get the description for this parameter.
    *
    * @return the parameter's description.
    */
   String getDescription();

   /**
    * Get the name of the parameter.
    *
    * @return the name.
    */
   String getName();

   /**
    * Get the value of the parameter.
    *
    * @return the value of the parameter.
    */
   T getValue();

   /**
    * Gets the string value of the parameter or an empty string if the value is {@code null}.
    *
    * @return the string value of the parameter or an empty string if the value is {@code null}
    */
   String getStringValue();

   /**
    * Get the category of the parameter.
    * 
    * @return the parameter category.
    */
   ParameterCategory getParameterCategory();
}
