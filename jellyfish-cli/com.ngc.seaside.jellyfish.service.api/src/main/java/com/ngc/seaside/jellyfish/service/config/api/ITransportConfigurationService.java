/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.jellyfish.service.config.api;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.config.api.dto.MulticastConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.zeromq.ZeroMqConfiguration;
import com.ngc.seaside.jellyfish.service.scenario.api.IMessagingFlow;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Collection;
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

}
