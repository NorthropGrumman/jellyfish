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
