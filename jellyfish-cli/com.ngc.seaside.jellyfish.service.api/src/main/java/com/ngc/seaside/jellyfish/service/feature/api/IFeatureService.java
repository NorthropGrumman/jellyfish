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
package com.ngc.seaside.jellyfish.service.feature.api;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.util.Collection;

/**
 * Service for dealing with feature files.
 */
public interface IFeatureService {

   /**
    * Returns all of the feature files associated with the given model.
    * 
    * @param options jellyfish command options
    * @param model the model
    * @return a collection of feature file information
    */
   Collection<IFeatureInformation> getFeatures(IJellyFishCommandOptions options, IModel model);

   /**
    * Returns all of the feature files associated with the given scenario.
    * 
    * @param options jellyfish command options
    * @param scenario the scenario to get the features for
    * @return a collection of feature file information
    */
   Collection<IFeatureInformation> getFeatures(IJellyFishCommandOptions options, IScenario scenario);

   /**
    * Returns all of the feature files for the system descriptor.
    * 
    * @param options jellyfish command options
    * @return a collection of feature file information
    */
   Collection<IFeatureInformation> getAllFeatures(IJellyFishCommandOptions options);
}
