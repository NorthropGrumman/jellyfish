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
package com.ngc.seaside.jellyfish.service.buildmgmt.api;

/**
 * Identifies the different type or scopes of dependencies.
 */
public enum DependencyScope {
   /**
    * Indicates a dependency is a normal dependency required to compile and/or run a project.  Dependencies in this
    * scope may be declared for compile, api, or implementation configurations.
    */
   BUILD,

   /**
    * Indicates a dependency is a normal dependency required to test a project.
    */
   TEST,

   /**
    * Indicates a dependency is needed by the build tool itself when a project is built.
    */
   BUILDSCRIPT
}
