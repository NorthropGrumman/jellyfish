package com.ngc.seaside.jellyfish.service.buildmgmt.api;

/**
 * Identifies the different type of dependencies.
 */
public enum DependencyType {
   /**
    * Indicates a dependency is a normal dependency required to compile and/or run a project.
    */
   NORMAL,

   /**
    * Indicates a dependency is needed by the build tool itself when a project is built.
    */
   BUILDSCRIPT
}
