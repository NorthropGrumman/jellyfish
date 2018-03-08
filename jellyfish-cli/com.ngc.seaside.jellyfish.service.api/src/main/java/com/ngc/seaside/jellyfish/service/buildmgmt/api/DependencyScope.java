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
