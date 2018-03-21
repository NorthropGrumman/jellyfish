package com.ngc.seaside.jellyfish.service.config.api;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.config.api.dto.MulticastConfiguration;
import com.ngc.seaside.jellyfish.service.scenario.api.IMessagingFlow;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;

import java.util.Collection;

public interface ITransportConfigurationService {
   String getTransportTopicName(IMessagingFlow flow, IDataReferenceField field);

   /**
    * Returns the multicast configurations for the given field, or an empty collection if there are no multicast configurations for the field.
    * 
    * @param options jellyfish options
    * @param field field
    * @return the multicast configurations for the given field
    */
   Collection<MulticastConfiguration> getMulticastConfiguration(IJellyFishCommandOptions options, IDataReferenceField field);
}
