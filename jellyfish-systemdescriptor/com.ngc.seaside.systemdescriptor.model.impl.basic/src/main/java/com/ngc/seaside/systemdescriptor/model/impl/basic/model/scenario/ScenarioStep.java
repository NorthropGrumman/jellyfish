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
