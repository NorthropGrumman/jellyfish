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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.multicast;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationDto.TransportTopicDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.multicast.MulticastConfigurationDto;
import com.ngc.seaside.jellyfish.service.config.api.dto.MulticastConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.NetworkInterface;

import java.util.Set;

public class MulticastTemplateTopicDto {

   private final MulticastConfiguration configuration;
   private final String topicVariableName;
   private final Set<TransportTopicDto> topics;
   private final boolean send;

   /**
    * Constructor.
    * 
    * @param transportTopicConfigurationDto transport topic configuration dto
    */
   public MulticastTemplateTopicDto(ITransportTopicConfigurationDto<
            MulticastConfigurationDto> transportTopicConfigurationDto, boolean send) {
      this.configuration = transportTopicConfigurationDto.getValue().getConfiguration();
      this.topicVariableName = transportTopicConfigurationDto.getTopicVariableName();
      this.topics = transportTopicConfigurationDto.getTransportTopics();
      this.send = send;
   }

   public String getVariableName() {
      return topicVariableName;
   }

   public Set<TransportTopicDto> getTransportTopics() {
      return topics;
   }

   public String getGroupAddress() {
      return configuration.getGroupAddress();
   }

   /**
    * Returns the address that the configured multicast topic should bind to.
    * 
    * @return the address that the configured multicast topic should bind to
    */
   public String getBindAddress() {
      NetworkInterface networkInterface =
               send ? configuration.getSourceInterface() : configuration.getTargetInterface();
      return networkInterface.getName();
   }

   public int getPort() {
      return configuration.getPort();
   }

   public boolean isSend() {
      return send;
   }
}
