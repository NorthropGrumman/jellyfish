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

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IFeature;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinScenario;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinTag;

import gherkin.ast.Background;
import gherkin.ast.Feature;
import gherkin.ast.Scenario;
import gherkin.ast.ScenarioDefinition;
import gherkin.ast.ScenarioOutline;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Contains a collection of scenarios defined in a single feature file.
 */
public class GherkinFeature extends AbstractWrappedGherkin<Feature> implements IFeature {

   private static final INamedChildCollection<IFeature, IGherkinScenario> EMPTY_SCENARIOS =
         NamedChildCollection.immutable(new NamedChildCollection<>());

   private final Collection<IGherkinTag> tags;

   private final INamedChildCollection<IFeature, IGherkinScenario> scenarios;

   private String name;

   private String packageName;

   private IScenario modelScenario;

   /**
    * Creates a new wrapped feature.
    *
    * @param wrapped the object to wrap
    * @param path    the path to source of the object
    */
   public GherkinFeature(Feature wrapped, Path path) {
      super(wrapped, path);
      tags = wrapped.getTags()
            .stream()
            .map(t -> new GherkinTag(t, path))
            .collect(Collectors.toList());

      IGherkinScenario background = null;
      scenarios = new NamedChildCollectionWithDuplicateNames<>();
      for (ScenarioDefinition definition : wrapped.getChildren()) {
         if (background == null && definition instanceof Background) {
            background = GherkinScenario.from((Background) definition, path, this);
         }

         if (definition instanceof Scenario) {
            scenarios.add(GherkinScenario.from((Scenario) definition, path, this, background));
         } else if (definition instanceof ScenarioOutline) {
            scenarios.add(GherkinScenario.from((ScenarioOutline) definition, path, this, background));
         }
      }
   }

   @Override
   public String getName() {
      return name;
   }

   public GherkinFeature setName(String name) {
      this.name = name;
      return this;
   }

   @Override
   public String getPackage() {
      return packageName;
   }

   public GherkinFeature setPackage(String packageName) {
      this.packageName = packageName;
      return this;
   }

   @Override
   public Optional<IScenario> getModelScenario() {
      return Optional.ofNullable(modelScenario);
   }

   public GherkinFeature setModelScenario(IScenario modelScenario) {
      this.modelScenario = modelScenario;
      return this;
   }

   @Override
   public String getShortDescription() {
      return wrapped.getName();
   }

   @Override
   public String getDescription() {
      return wrapped.getDescription();
   }

   @Override
   public INamedChildCollection<IFeature, IGherkinScenario> getScenarios() {
      return NamedChildCollection.immutable(scenarios);
   }

   @Override
   public Collection<IGherkinTag> getTags() {
      return Collections.unmodifiableCollection(tags);
   }

   @Override
   public boolean hasTag(String tagName) {
      Preconditions.checkNotNull(tagName, "tagName may not be null");
      return tags.stream().anyMatch(t -> t.getName().equals(tagName));
   }

   @Override
   public String getFullyQualifiedName() {
      return packageName + "." + name;
   }

   /**
    * Creates an empty feature.
    *
    * @param packageName the package name of the feature
    * @param name        the name of the feature
    * @param path        source file of the feature
    * @return an empty feature
    */
   public static IFeature emptyFeature(String packageName,
                                       String name,
                                       IScenario scenario,
                                       Path path) {
      return new IFeature() {
         @Override
         public Optional<IScenario> getModelScenario() {
            return Optional.ofNullable(scenario);
         }

         @Override
         public String getName() {
            return name;
         }

         @Override
         public String getPackage() {
            return packageName;
         }

         @Override
         public String getShortDescription() {
            return null;
         }

         @Override
         public String getDescription() {
            return null;
         }

         @Override
         public INamedChildCollection<IFeature, IGherkinScenario> getScenarios() {
            return EMPTY_SCENARIOS;
         }

         @Override
         public Collection<IGherkinTag> getTags() {
            return Collections.emptyList();
         }

         @Override
         public boolean hasTag(String tagName) {
            return false;
         }

         @Override
         public String getFullyQualifiedName() {
            return packageName + "." + name;
         }
      };
   }
}
