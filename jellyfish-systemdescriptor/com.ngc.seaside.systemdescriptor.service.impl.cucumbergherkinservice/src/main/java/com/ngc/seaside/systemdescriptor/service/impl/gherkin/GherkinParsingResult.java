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
package com.ngc.seaside.systemdescriptor.service.impl.gherkin;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.service.api.IParsingIssue;
import com.ngc.seaside.systemdescriptor.service.gherkin.api.IGherkinParsingResult;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IFeature;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinScenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Contains the result of parsing feature files.
 */
public class GherkinParsingResult implements IGherkinParsingResult {

   private final Collection<IFeature> features = new ArrayList<>();

   private final Collection<IParsingIssue> issues = new ArrayList<>();

   @Override
   public Collection<IFeature> getFeatures() {
      return Collections.unmodifiableCollection(features);
   }

   /**
    * Adds the given features to this result.
    *
    * @param features the features to add
    * @return this result
    */
   public GherkinParsingResult addFeatures(Collection<IFeature> features) {
      this.features.addAll(features);
      return this;
   }

   /**
    * Adds a feature to this result.
    *
    * @param feature the feature to add
    * @return this result
    */
   public GherkinParsingResult addFeature(IFeature feature) {
      this.features.add(feature);
      return this;
   }

   @Override
   public Optional<IFeature> findFeature(String fullyQualifiedName) {
      Preconditions.checkNotNull(fullyQualifiedName, "fullyQualifiedName may not be null!");
      Preconditions.checkArgument(!fullyQualifiedName.trim().isEmpty(), "fullyQualifiedName may not be empty!");
      return features.stream().filter(f -> f.getFullyQualifiedName().equals(fullyQualifiedName)).findFirst();
   }

   @Override
   public Optional<IFeature> findFeature(String packageName, String featureName) {
      Preconditions.checkNotNull(packageName, "packageName may not be null!");
      Preconditions.checkArgument(!packageName.trim().isEmpty(), "packageName may not be empty!");
      Preconditions.checkNotNull(featureName, "featureName may not be null!");
      Preconditions.checkArgument(!featureName.trim().isEmpty(), "featureName may not be empty!");
      return findFeature(packageName + "." + featureName);
   }

   @Override
   public Optional<IFeature> findFeature(IScenario scenario) {
      Preconditions.checkNotNull(scenario, "scenario may not be null!");
      return features.stream().filter(f -> scenario.equals(f.getModelScenario().orElse(null))).findFirst();
   }

   @Override
   public Collection<IFeature> findFeatures(IModel model) {
      Preconditions.checkNotNull(model, "model may not be null!");
      Collection<IFeature> filtered = new ArrayList<>();
      for (IFeature feature : features) {
         String[] name = feature.getName().split("\\.");
         if (feature.getPackage().equals(model.getParent().getName())
                   && name.length > 0
                   && model.getName().equals(name[0])) {
            filtered.add(feature);
         }
      }
      return Collections.unmodifiableCollection(filtered);
   }

   @Override
   public Collection<IFeature> findFeatures(Collection<String> tags) {
      Preconditions.checkNotNull(tags, "tags may not be null!");
      return features.stream()
            .filter(f -> f.getTags().stream().anyMatch(t -> tags.contains(t.getName())))
            .collect(Collectors.toList());
   }

   @Override
   public Collection<IFeature> findFeatures(String tag, String... tags) {
      Preconditions.checkNotNull(tag, "tag may not be null!");
      Collection<String> collection = new ArrayList<>();
      collection.add(tag);
      if (tags != null) {
         Collections.addAll(collection, tags);
      }
      return findFeatures(collection);
   }

   @Override
   public Collection<IGherkinScenario> findScenarios(Collection<String> tags) {
      Preconditions.checkNotNull(tags, "tags may not be null!");
      Collection<IGherkinScenario> scenarios = new ArrayList<>();
      for (IFeature feature : features) {
         for (IGherkinScenario scenario : feature.getScenarios()) {
            for (String tag : tags) {
               if (scenario.hasTag(tag)) {
                  scenarios.add(scenario);
               }
            }
         }
      }
      return Collections.unmodifiableCollection(scenarios);
   }

   @Override
   public Collection<IGherkinScenario> findScenarios(String tag, String... tags) {
      Preconditions.checkNotNull(tag, "tag may not be null!");
      Collection<String> collection = new ArrayList<>();
      collection.add(tag);
      if (tags != null) {
         Collections.addAll(collection, tags);
      }
      return findScenarios(collection);
   }

   @Override
   public boolean isSuccessful() {
      return issues.isEmpty();
   }

   @Override
   public Collection<IParsingIssue> getIssues() {
      return Collections.unmodifiableCollection(issues);
   }

   /**
    * Adds the given issues to this result.
    *
    * @param issues the issues to add
    * @return this result
    */
   public GherkinParsingResult addIssues(Collection<IParsingIssue> issues) {
      this.issues.addAll(issues);
      return this;
   }

   /**
    * Adds an issue to this result.
    *
    * @param issue the issue to add
    * @return this result
    */
   public GherkinParsingResult addIssue(IParsingIssue issue) {
      this.issues.add(issue);
      return this;
   }
}
