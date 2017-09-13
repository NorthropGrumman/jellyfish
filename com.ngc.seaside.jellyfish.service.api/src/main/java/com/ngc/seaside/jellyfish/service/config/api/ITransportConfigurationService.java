package com.ngc.seaside.jellyfish.service.config.api;

import com.ngc.seaside.jellyfish.service.scenario.api.IMessagingFlow;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;

/**
 * Assists with the generation of Java transport configuration code.
 */
public interface ITransportConfigurationService {

   /**
    * Gets the name of the transport topic for the given flow and field.
    * 
    * @param flow the messaging flow containing the given field
    * @param field the transport topic field
    * @return the name of the transport topic
    */
   String getTransportTopicName(IMessagingFlow flow, IDataReferenceField field);
}
