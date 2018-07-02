package com.ngc.seaside.jellyfish.service.config.api;

import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.config.api.dto.MulticastConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.TelemetryConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.TelemetryReportingConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.zeromq.ZeroMqConfiguration;
import com.ngc.seaside.jellyfish.service.scenario.api.IMessagingFlow;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * The {@code ITransportConfigurationService} is used to determine transport related information from a model.
 */
public interface ITransportConfigurationService {

   /**
    * Gets the name of the transport topic that should be used for the given field which is part of the given flow.
    *
    * @param flow  the flow that references the field
    * @param field the field to generate the topic for
    * @return the name of the transport topic
    * @throws IllegalArgumentException if the given flow does not reference {@code field}
    */
   String getTransportTopicName(IMessagingFlow flow, IDataReferenceField field);

   /**
    * Gets the name of the transport topic that should be used for getting the telemetry of the model with the given
    * part, or {@link Optional#empty()} if there is no transport topic associated with the given model part.
    * 
    * @param options jellyfish options
    * @param part model part
    * @return the name of the transport topic that should be used for getting the telemetry of the model with the given
    *         part
    */
   Optional<String> getTelemetryTransportTopicName(IJellyFishCommandOptions options, IModelReferenceField part);

   /**
    * Gets the name of the transport topic that should be used for reporting the telemetry of a service, or
    * {@link Optional#empty()} if there is no telemetry reporting transport topic.
    * 
    * @param options jellyfish options
    * @param model model
    * @return the name of the transport topic that should be used for reporting the telemetry of a service
    */
   Optional<String> getTelemetryReportingTransportTopicName(IJellyFishCommandOptions options, IModel model);

   /**
    * Returns the transport configuration types used by the given model with the given deployment model.
    *
    * @param options jellyfish options
    * @param model model
    * @return the transport configuration types used by the given deployment model
    */
   Set<TransportConfigurationType> getConfigurationTypes(IJellyFishCommandOptions options, IModel model);

   /**
    * Returns the multicast configurations for the given field, or an empty collection if there are no multicast
    * configurations for the field.
    *
    * @param options jellyfish options
    * @param field   field
    * @return the multicast configurations for the given field
    */
   Collection<MulticastConfiguration> getMulticastConfiguration(IJellyFishCommandOptions options,
                                                                IDataReferenceField field);

   /**
    * Returns the rest configurations for the given field, or an empty collection if there are no rest configurations
    * for the field.
    *
    * @param options jellyfish options
    * @param field   field
    * @return the rest configurations for the given field
    */
   Collection<RestConfiguration> getRestConfiguration(IJellyFishCommandOptions options, IDataReferenceField field);

   /**
    * Returns the Zero MQ configurations for the given field, or an empty collection if there are no rest configurations
    * for the field.
    *
    * @param options jellyfish options
    * @param field   field
    * @return the Zero MQ configurations for the given field
    */
   Collection<ZeroMqConfiguration> getZeroMqConfiguration(IJellyFishCommandOptions options, IDataReferenceField field);

   /**
    * Returns the telemetry configurations for the given model, or an empty collection if there are no telemetry
    * configurations for the model. The telemetry configurations can be found in the model's own properties, or as a
    * part in the jellyfish command option's {@link CommonParameters#DEPLOYMENT_MODEL}.
    * 
    * @param options jellyfish options
    * @param model   model
    * @return the telemetry configurations for the given model
    */
   Collection<TelemetryConfiguration> getTelemetryConfiguration(IJellyFishCommandOptions options,
            IModel model);

   /**
    * Returns the telemetry reporting configurations for the given model, or an empty collection if there are no
    * telemetry reporting configurations for the model. The telemetry reporting configurations can be found in the
    * model's own properties, or as a part in the jellyfish command option's {@link CommonParameters#DEPLOYMENT_MODEL}.
    * 
    * @param options jellyfish options
    * @param model model
    * @return the telemetry reporting configurations for the given model
    */
   Collection<TelemetryReportingConfiguration> getTelemetryReportingConfiguration(IJellyFishCommandOptions options,
            IModel model);
}
