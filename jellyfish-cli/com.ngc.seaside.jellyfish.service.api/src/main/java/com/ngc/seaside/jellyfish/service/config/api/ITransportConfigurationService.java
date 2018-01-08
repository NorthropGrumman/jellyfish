package com.ngc.seaside.jellyfish.service.config.api;

import com.ngc.seaside.jellyfish.service.scenario.api.IMessagingFlow;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;

public interface ITransportConfigurationService {
   String getTransportTopicName(IMessagingFlow flow, IDataReferenceField field);
}
