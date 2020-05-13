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
package com.ngc.seaside.systemdescriptor.service.impl.gherkin.model;

import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.GherkinStepKeyword;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinScenario;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinStep;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinTable;

import gherkin.ast.DataTable;
import gherkin.ast.DocString;
import gherkin.ast.Node;
import gherkin.ast.Step;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * A single instruction in a scenario or background.
 */
public class GherkinStep extends AbstractWrappedGherkin<Step> implements IGherkinStep {

   private final IGherkinScenario parent;

   private final IGherkinTable tableArgument;

   private final List<String> docStringArgument;

   private final GherkinStepKeyword keyword;

   /**
    * Creates a new wrapped step.
    *
    * @param wrapped the step to wrap
    * @param path    the source file of the step
    * @param parent  the scenario that contains the step
    */
   public GherkinStep(Step wrapped,
                      Path path,
                      IGherkinScenario parent) {
      super(wrapped, path);
      this.parent = parent;
      this.keyword = GherkinStepKeyword.valueOf(wrapped.getKeyword().toUpperCase().trim());

      Node argument = wrapped.getArgument();
      if (argument instanceof DataTable) {
         tableArgument = GherkinTable.from((DataTable) argument, path);
         docStringArgument = Collections.emptyList();
      } else if (argument instanceof DocString) {
         tableArgument = null;
         docStringArgument = Arrays.asList(((DocString) argument).getContent().split("[\\r\\n]+"));
      } else {
         tableArgument = null;
         docStringArgument = Collections.emptyList();
      }
   }

   @Override
   public IGherkinScenario getParent() {
      return parent;
   }

   @Override
   public GherkinStepKeyword getKeyword() {
      return keyword;
   }

   @Override
   public String getContent() {
      return wrapped.getText();
   }

   @Override
   public Optional<IGherkinTable> getTableArgument() {
      return Optional.of(tableArgument);
   }

   @Override
   public List<String> getDocStringArgument() {
      return Collections.unmodifiableList(docStringArgument);
   }
}
