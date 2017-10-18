package com.ngc.seaside.jellyfish.service.feature.api;

import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.nio.file.Path;
import java.util.Collection;
import java.util.TreeMap;

/**
 * Service for dealing with feature files.
 * @author bperkins
 */
public interface IFeatureService {
   
   /**
    * Gets a map of all the features for the given system descriptor path and model
    * @param sdPath the path to the system descriptor
    * @param model the model
    * @return a map of the feature information
    */
   TreeMap<String, IFeatureInformation> getFeatures(Path sdPath, IModel model);

   /**
    * Gets a map of all the features for the given scenario.
    * @param sdPath the path to the system descriptor
    * @param scenario the scenario to get the features for
    * @return a map of the feature information for the given scenario
    */
   TreeMap<String, IFeatureInformation> getFeatures(Path sdPath, IScenario scenario);

   /**
    * Gets a map of all the features for the given system descriptor path and a collection of models
    * @param sdPath the path to the system descriptor
    * @param models the collection of models
    * @return map of the feature information
    */
   TreeMap<String, IFeatureInformation> getAllFeatures(Path sdPath, Collection<IModel> models);
}
