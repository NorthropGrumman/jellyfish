package com.ngc.seaside.jellyfish.service.config.api;

import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Collection;

public interface IModelPropertyConfigurationService<T> {

   /**
    * Returns the configurations this service's given type for the given model, or an empty collection if there are no
    * configurations for the model. The model property configurations can be found in the model's own properties, or as
    * a part in the jellyfish command option's {@link CommonParameters#DEPLOYMENT_MODEL}.
    * 
    * @param options jellyfish options
    * @param model model
    * @return the configurations for the given model
    */
   Collection<T> getConfigurations(IJellyFishCommandOptions options, IModel model);
   
}
