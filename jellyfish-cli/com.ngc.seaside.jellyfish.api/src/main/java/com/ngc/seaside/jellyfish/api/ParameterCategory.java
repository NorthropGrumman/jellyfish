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
 * An enumeration describing the "usefulness" of a jellyfish command parameter to a typical user
 */
public enum ParameterCategory {
   /**
    * Represents a parameter that is required to run a command.
    */
   REQUIRED,
   
   /**
    * Represents a parameter that is not required, but the user might want to specify.
    */
   OPTIONAL,
   
   /**
    * Represents a parameter that only an advanced jellyfish user will ever want to use.
    */
   ADVANCED
}
