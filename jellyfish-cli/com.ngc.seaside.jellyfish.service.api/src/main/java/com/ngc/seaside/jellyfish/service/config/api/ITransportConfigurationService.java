package com.ngc.seaside.jellyfish.service.config.api;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.config.api.dto.MulticastConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;
import com.ngc.seaside.jellyfish.service.scenario.api.IMessagingFlow;

import java.util.Collection;
import java.util.Set;

/**
 * The {@code ITransportConfigurationService} is used to determine transport related information from a model.
 */
public interface ITransportConfigurationService {

   /**
    * Gets the name of the transport topic that should be used for the given field which is part of the given flow.
    *
    * @param flow the flow that references the field
    * @return the name of the transport topic
    * @throws IllegalArgumentException if the given flow does not reference {@code field}
    */
   String getTransportTopicName(IMessagingFlow flow);

   /**
    * Returns the transport configuration types used by the given deployment model.
    *
    * @param options jellyfish options
    * @return the transport configuration types used by the given deployment model
    */
   Set<TransportConfigurationType> getConfigurationTypes(IJellyFishCommandOptions options);

   /**
    * Returns the multicast configurations for the given field, or an empty collection if there are no multicast
    * configurations for the field.
    *
    * @param options jellyfish options
    * @return the multicast configurations for the given field
    */
   Collection<MulticastConfiguration> getMulticastConfiguration(IJellyFishCommandOptions options);

   /**
    * Returns the rest configurations for the given field, or an empty collection if there are no rest configurations
    * for the field.
    *
    * @param options jellyfish options
    * @return the rest configurations for the given field
    */
   Collection<RestConfiguration> getRestConfiguration(IJellyFishCommandOptions options);
}
