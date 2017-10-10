package com.ngc.seaside.jellyfish.service.feature.api;

/**
 * Service for dealing with feature files.
 * @author bperkins
 */
public interface IFeatureService {

   /**
    * Gets the feature information with the given path.
    * @param uri the path to the feature files
    * @return the feature file info
    */
   IFeatureInformation getFeatureInfo(String uri);

}
