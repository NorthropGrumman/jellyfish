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
package com.ngc.seaside.jellyfish.utilities.command;

/**
 * Defines the different phases a command can be executed in.
 */
public enum JellyfishCommandPhase {
   /**
    * The default phase occurs when Jellyfish should generate an initial project and stubs only.  These stubs may be
    * later modified but will never be regenerated.  Fully generated code should not be generated in this phase.
    */
   DEFAULT,

   /**
    * The deferred phase occurs when Jellyfish should generate fully generated code but not generate any stubs.  Only
    * fully generated code that is never modified should be generated in this phase.
    */
   DEFERRED,
}
