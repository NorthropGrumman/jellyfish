/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
