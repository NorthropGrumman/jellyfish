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
