package com.ngc.seaside.jellyfish.cli.command.test.service;

import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.jellyfish.service.scenario.api.IMessagingFlow;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;

public class MockedTransportConfigurationService implements ITransportConfigurationService {

   @Override
   public String getTransportTopicName(IMessagingFlow flow, IDataReferenceField field) {
      return field.getType().getName().toUpperCase();
   }

}
