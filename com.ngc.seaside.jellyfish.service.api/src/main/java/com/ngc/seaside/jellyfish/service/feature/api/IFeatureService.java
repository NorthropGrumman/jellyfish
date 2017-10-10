package com.ngc.seaside.jellyfish.service.feature.api;

import com.ngc.seaside.jellyfish.service.feature.api.dto.FeatureDto;

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
   FeatureDto getFeatureInfo(String uri);

}
