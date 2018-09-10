/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
