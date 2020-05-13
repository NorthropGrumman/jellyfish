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

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.GherkinStepKeyword;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IFeature;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinScenario;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinStep;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinTable;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinTag;

import gherkin.ast.Background;
import gherkin.ast.Scenario;
import gherkin.ast.ScenarioDefinition;
import gherkin.ast.ScenarioOutline;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A single scenario made up of steps.
 */
public class GherkinScenario extends AbstractWrappedGherkin<ScenarioDefinition> implements IGherkinScenario {

   private final IFeature parent;

   private final List<IGherkinStep> steps;

   private final List<IGherkinStep> givens;

   private final List<IGherkinStep> whens;

   private final List<IGherkinStep> thens;

   private final IGherkinScenario background;

   private final IGherkinTable examples;

   private final Collection<IGherkinTag> tags;

   private GherkinScenario(ScenarioDefinition wrapped,
                           Path path,
                           IFeature parent,
                           IGherkinScenario background,
                           IGherkinTable examples,
                           Collection<IGherkinTag> tags) {
      super(wrapped, path);
      this.parent = parent;
      this.background = background;
      this.examples = examples;
      this.tags = tags;

      this.steps = wrapped.getSteps()
            .stream()
            .map(s -> new GherkinStep(s, path, this))
            .collect(Collectors.toList());
      this.givens = convertSteps(GherkinStepKeyword.GIVEN, this.steps);
      this.whens = convertSteps(GherkinStepKeyword.WHEN, this.steps);
      this.thens = convertSteps(GherkinStepKeyword.THEN, this.steps);
   }

   @Override
   public IFeature getParent() {
      return parent;
   }

   @Override
   public String getName() {
      return wrapped.getName();
   }

   @Override
   public String getDescription() {
      return wrapped.getDescription();
   }

   @Override
   public Collection<IGherkinTag> getTags() {
      return Collections.unmodifiableCollection(tags);
   }

   @Override
   public boolean hasTag(String tagName) {
      Preconditions.checkNotNull(tagName, "tagName may not be null");
      return tags.stream().anyMatch(t -> t.getName().equals(tagName))
             || parent.getTags().stream().anyMatch(t -> t.getName().equals(tagName));
   }

   @Override
   public List<IGherkinStep> getSteps() {
      return Collections.unmodifiableList(steps);
   }

   @Override
   public List<IGherkinStep> getGivens() {
      return Collections.unmodifiableList(givens);
   }

   @Override
   public List<IGherkinStep> getWhens() {
      return Collections.unmodifiableList(whens);
   }

   @Override
   public List<IGherkinStep> getThens() {
      return Collections.unmodifiableList(thens);
   }

   @Override
   public Optional<IGherkinScenario> getBackground() {
      return Optional.ofNullable(background);
   }

   @Override
   public Optional<IGherkinTable> getExamples() {
      return Optional.ofNullable(examples);
   }

   @Override
   public boolean isOutline() {
      return wrapped instanceof ScenarioOutline;
   }

   /**
    * Creates a new scenario from Gherkin scenario with an optional background.
    *
    * @param scenario   the scenario to wrap
    * @param path       the source file of the scenario
    * @param parent     the feature file that contains the scenario
    * @param background the optional background steps of the scenario
    * @return the wrapped scenario
    */
   public static GherkinScenario from(Scenario scenario, Path path, IFeature parent, IGherkinScenario background) {
      return new GherkinScenario(scenario,
                                 path,
                                 parent,
                                 background,
                                 null,
                                 scenario.getTags()
                                       .stream()
                                       .map(t -> new GherkinTag(t, path))
                                       .collect(Collectors.toList()));
   }

   /**
    * Creates a new scenario from an outline.
    *
    * @param outline    the outline to wrap
    * @param path       the source file of the outline
    * @param parent     the feature file that contains the outline
    * @param background the optional background steps of the output
    * @return the wrapped scenario
    */
   public static GherkinScenario from(ScenarioOutline outline,
                                      Path path,
                                      IFeature parent,
                                      IGherkinScenario background) {
      IGherkinTable examples = outline.getExamples().isEmpty()
                               ? null
                               : GherkinTable.from(outline.getExamples().get(0), path);
      return new GherkinScenario(outline,
                                 path,
                                 parent,
                                 background,
                                 examples,
                                 outline.getTags()
                                       .stream()
                                       .map(t -> new GherkinTag(t, path))
                                       .collect(Collectors.toList()));
   }

   /**
    * Creates a new wrapped scenario for backgrounds.
    *
    * @param background the background steps
    * @param path       the source file of the scenario
    * @param parent     the feature that contains the scenario
    * @return the scenario
    */
   public static GherkinScenario from(Background background, Path path, IFeature parent) {
      return new GherkinScenario(background,
                                 path,
                                 parent,
                                 null,
                                 null,
                                 Collections.emptyList());
   }

   private List<IGherkinStep> convertSteps(GherkinStepKeyword keyword, Collection<IGherkinStep> steps) {
      List<IGherkinStep> filtered = new ArrayList<>();

      GherkinStepKeyword previousKeyword = null;
      for (IGherkinStep step : steps) {
         GherkinStepKeyword currentKeyword = step.getKeyword();
         boolean matches = currentKeyword == keyword;
         matches |= (currentKeyword == GherkinStepKeyword.AND || currentKeyword == GherkinStepKeyword.BUT)
                    && previousKeyword == keyword;
         if (matches) {
            filtered.add(step);
         }
         previousKeyword = currentKeyword;
      }

      return filtered;
   }
}
