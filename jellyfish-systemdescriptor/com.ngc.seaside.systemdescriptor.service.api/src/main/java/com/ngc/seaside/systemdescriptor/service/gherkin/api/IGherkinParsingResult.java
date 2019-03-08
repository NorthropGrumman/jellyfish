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
package com.ngc.seaside.systemdescriptor.service.gherkin.api;

import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.service.api.IParsingIssue;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IFeature;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinScenario;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by the {@link IGherkinService} as a result of parsing Gherkin feature files.
 */
public interface IGherkinParsingResult {

   /**
    * Gets the features that were parsed.  This collection may be empty if parsing was not {@link #isSuccessful()
    * successful}.
    *
    * @return the features that were parsed
    */
   Collection<IFeature> getFeatures();

   /**
    * Finds the feature with the given fully qualified name.
    * @param fullyQualifiedName the fully qualified name of the feature
    * @return an optional that contains the given feature or an empty optional of the feature was not found
    * @see IFeature#getFullyQualifiedName()
    */
   Optional<IFeature> findFeature(String fullyQualifiedName);

   /**
    * Finds the feature with the given package and name.
    * @param packageName the name of the package that contains the feature
    * @param featureName the name of the feature
    * @return the feature with the given package and name or an empty optional if no such feature was found
    */
   Optional<IFeature> findFeature(String packageName, String featureName);

   /**
    * Finds the Gherkin feature associated with the given System Descriptor scenario, provided the scenario has a
    * feature.
    *
    * @param scenario the scenario to get the feature for
    * @return the Gherkin feature associated with the given System Descriptor scenario or an empty optional of the
    * scenario has no feature
    */
   Optional<IFeature> findFeature(IScenario scenario);

   /**
    * Finds all Gherkin features associated with the given model.
    * @param model the model to get the features for
    * @return a list of features associated with the model
    */
   Collection<IFeature> findFeatures(IModel model);

   /**
    * Finds all features which have the one or more of the given tags.  Features will be included in the result if
    * either they contain at least one of the given tags or they contain a scenario which contains at least one of the
    * given tags.
    *
    * @param tags the tags to filter the features by
    * @return all features which have the one or more of the given tags
    */
   Collection<IFeature> findFeatures(Collection<String> tags);

   /**
    * Finds all features which have the one or more of the given tags.  Features will be included in the result if
    * either they contain at least one of the given tags or they contain a scenario which contains at least one of the
    * given tags.
    *
    * @param tag  the tag to filter the features by
    * @param tags the tags to filter the features by
    * @return all features which have the one or more of the given tags
    */
   Collection<IFeature> findFeatures(String tag, String... tags);

   /**
    * Finds all scenarios which have the one or more of the given tags.  Scenario will be included in the result if
    * either they contain at least one of the given tags or their parent feature contains at least one of the given
    * tags.
    *
    * @param tags the tags to filter the scenarios by
    * @return all scenarios which have the one or more of the given tags
    */
   Collection<IGherkinScenario> findScenarios(Collection<String> tags);

   /**
    * Finds all scenarios which have the one or more of the given tags.  Scenario will be included in the result if
    * either they contain at least one of the given tags or their parent feature contains at least one of the given
    * tags.
    *
    * @param tag  the tag to filter the scenarios by
    * @param tags the tags to filter the scenarios by
    * @return all scenarios which have the one or more of the given tags
    */
   Collection<IGherkinScenario> findScenarios(String tag, String... tags);

   /**
    * Returns true if the Gherkin feature files were successfully parsed without errors. If false is returned, the
    * {@code IFeature}s associated with this result may not be valid or may only be partially complete.
    *
    * @return true if the Gherkin feature files were successfully parsed without errors, false if there are errors
    */
   boolean isSuccessful();

   /**
    * Gets all issues associated with the parsed Gherkin files. If there are no issues, an empty collection is
    * returned.
    *
    * @return an unmodifiable collection of parsing related issues
    */
   Collection<IParsingIssue> getIssues();
}
