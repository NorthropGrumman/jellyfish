package com.ngc.seaside.jellyfish.service.buildmgmt.api;

/**
 * Represents a dependency a generated project has.
 */
public interface IBuildDependency {

   /**
    * Gets the group ID of the dependency.
    */
   String getGroupId();

   /**
    * Gets the artifact ID of the dependency.
    */
   String getArtifactId();

   /**
    * Gets the version of the dependency.
    */
   String getVersion();

   /**
    * Gets the name of the property that can be used to identify the version of this dependency.
    */
   String getVersionPropertyName();
}
