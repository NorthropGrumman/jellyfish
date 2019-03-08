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
