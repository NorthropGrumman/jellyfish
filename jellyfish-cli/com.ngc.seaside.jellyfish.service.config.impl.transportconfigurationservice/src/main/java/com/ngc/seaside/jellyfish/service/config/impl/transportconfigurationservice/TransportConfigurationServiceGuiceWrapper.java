package com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice;

import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.jellyfish.service.scenario.api.IMessagingFlow;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;

public class TransportConfigurationServiceGuiceWrapper implements ITransportConfigurationService {

   private final TransportConfigurationService delegate;

   @Inject
   public TransportConfigurationServiceGuiceWrapper(ILogService logService) {
      this.delegate = new TransportConfigurationService();
      this.delegate.setLogService(logService);
      this.delegate.activate();
   }

   @Override
   public String getTransportTopicName(IMessagingFlow flow, IDataReferenceField field) {
      return this.delegate.getTransportTopicName(flow, field);
   }

}
