package com.ngc.seaside.jellyfish.service.feature.api;

import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.nio.file.Path;

/**
 * Service for dealing with feature files.
 * @author bperkins
 */
public interface IFeatureService {

   /**
    * Gets the feature information with the given system descriptor path and model.
    * @param sdPath the path to the system descriptor
    * @return model the model
    */
   IFeatureInformation getFeatureInfo(Path sdPath, IModel model);

}
