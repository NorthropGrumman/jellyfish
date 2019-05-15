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
