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
