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
package com.ngc.seaside.jellyfish.cli.gradle;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;

public class DerivedRootProjectPartsExtension {

   private final Project project;

   private String model;
   private Dependency distribution;

   DerivedRootProjectPartsExtension(Project project) {
      this.project = project;
   }

   /**
    * Returns the system descriptor part model name.
    * 
    * @return the system descriptor part model name
    */
   public String getModel() {
      return model;
   }

   public DerivedRootProjectPartsExtension setModel(String model) {
      this.model = model;
      return this;
   }

   /**
    * Returns the system descriptor part distribution dependency.
    * 
    * @return the system descriptor part distribution dependency
    */
   public Dependency getDistribution() {
      return distribution;
   }

   /**
    * Sets the system descriptor part distribution dependency.
    * 
    * @param dependency the system descriptor part distribution dependency
    * @return this
    */
   public DerivedRootProjectPartsExtension setDistribution(Object dependency) {
      if (dependency == null) {
         distribution = null;
      } else {
         distribution = project.getDependencies().create(dependency);
      }
      return this;
   }

}
