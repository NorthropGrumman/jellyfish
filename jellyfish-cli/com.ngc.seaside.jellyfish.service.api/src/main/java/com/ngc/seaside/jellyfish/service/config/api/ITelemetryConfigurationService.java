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
package com.ngc.seaside.jellyfish.service.config.api;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.TelemetryConfiguration;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;

import java.util.Optional;

/**
 * Marker interface for a service that can parse a model for telemetry configurations.
 */
public interface ITelemetryConfigurationService extends IModelPropertyConfigurationService<TelemetryConfiguration> {

   /**
    * Gets the name of the transport topic that should be used for getting the telemetry of the model with the given
    * part, or {@link Optional#empty()} if there is no transport topic associated with the given model part.
    * 
    * @param options jellyfish options
    * @param part model part
    * @return the name of the transport topic that should be used for getting the telemetry of the model with the given
    *         part
    */
   Optional<String> getTransportTopicName(IJellyFishCommandOptions options, IModelReferenceField part);

}
