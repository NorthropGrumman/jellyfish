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
package com.ngc.seaside.systemdescriptor.model.impl.basic.model.scenario;

import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Implements the IScenarioStep interface. Maintains the list of parameters for the step
 * along with the keyword (Given, When, or Then)
 *
 * @author psnell
 */
public class ScenarioStep implements IScenarioStep {

   private final List<String> parameters;
   private IScenario parent;
   private String keyword;

   private ScenarioStep(List<String> parameters) {
      this.parameters = parameters;
   }

   /**
    * Creates a new step.
    */
   public ScenarioStep() {
      this.parameters = new ArrayList<>();
   }

   @Override
   public String getKeyword() {
      return keyword;
   }

   @Override
   public IScenarioStep setKeyword(String keyword) {
      this.keyword = keyword;
      return this;
   }

   @Override
   public List<String> getParameters() {
      return parameters;
   }

   @Override
   public IScenario getParent() {
      return parent;
   }

   public ScenarioStep setParent(IScenario parent) {
      this.parent = parent;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof ScenarioStep)) {
         return false;
      }
      ScenarioStep that = (ScenarioStep) o;
      return Objects.equals(parameters, that.parameters)
             && Objects.equals(keyword, that.keyword)
             && parent == that.parent;
   }

   @Override
   public int hashCode() {
      return Objects.hash(parameters,
                          keyword,
                          System.identityHashCode(parent));
   }

   @Override
   public String toString() {
      return "ScenarioStep["
             + "parameters=" + parameters
             + ", keyword='" + keyword + '\''
             + ", parent=" + (parent == null ? "null" : parent.getName())
             + ']';
   }

}
