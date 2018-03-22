package com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice;

import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.dto.MulticastConfiguration;
import com.ngc.seaside.jellyfish.service.scenario.api.IMessagingFlow;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;

import java.util.Collection;

public class TransportConfigurationServiceGuiceWrapper implements ITransportConfigurationService {

   private final TransportConfigurationService delegate;

   @Inject
   public TransportConfigurationServiceGuiceWrapper(ILogService logService, ISystemDescriptorService sdService) {
      this.delegate = new TransportConfigurationService();
      this.delegate.setLogService(logService);
      this.delegate.setSystemDescriptorService(sdService);
      this.delegate.activate();
   }

   @Override
   public String getTransportTopicName(IMessagingFlow flow, IDataReferenceField field) {
      return this.delegate.getTransportTopicName(flow, field);
   }

   @Override
   public Collection<MulticastConfiguration> getMulticastConfiguration(IJellyFishCommandOptions options,
            IDataReferenceField field) {
      return delegate.getMulticastConfiguration(options, field);
   }

}
