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
package com.ngc.seaside.systemdescriptor.validation.api;

/**
 * The various serenity levels associated with issues.
 */
public enum Severity {
   /**
    * The highest serenity level; indicates an fatal error and the descriptor is invalid.
    */
   ERROR,
   /**
    * Indicates a potential problem but the descriptor is valid.
    */
   WARNING,
   /**
    * Indicates a suggested fix to a valid descriptor.
    */
   SUGGESTION
}
