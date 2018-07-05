package com.ngc.seaside.jellyfish.service.config.api;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.TelemetryReportingConfiguration;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Optional;

/**
 * Marker interface for a service that can parse a model for telemetry reporting configurations.
 */
public interface ITelemetryReportingConfigurationService
         extends IModelPropertyConfigurationService<TelemetryReportingConfiguration> {

   /**
    * Gets the name of the transport topic that should be used for reporting the telemetry of a service, or
    * {@link Optional#empty()} if there is no telemetry reporting transport topic.
    * 
    * @param options jellyfish options
    * @param model model
    * @return the name of the transport topic that should be used for reporting the telemetry of a service
    */
   Optional<String> getTransportTopicName(IJellyFishCommandOptions options, IModel model);

}
