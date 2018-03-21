package com.ngc.seaside.jellyfish.service.config.api;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.config.api.dto.MulticastConfiguration;
import com.ngc.seaside.jellyfish.service.scenario.api.IMessagingFlow;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;

import java.util.Optional;

public interface ITransportConfigurationService {
   String getTransportTopicName(IMessagingFlow flow, IDataReferenceField field);

   /**
    * Returns the multicast configuration for the given field, or {@link Optional#empty()} if there is no multicast configuration for the field.
    * 
    * @param options jellyfish options
    * @param field field
    * @return the multicast configuration for the given field
    */
   Optional<MulticastConfiguration> getMulticastConfiguration(IJellyFishCommandOptions options, IDataReferenceField field);
}
