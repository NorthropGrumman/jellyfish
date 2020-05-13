/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
