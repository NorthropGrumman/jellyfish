package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.httpclient;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationDto.TransportTopicDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.rest.RestConfigurationDto;
import com.ngc.seaside.jellyfish.service.config.api.dto.HttpMethod;
import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;

import java.util.Set;

public class HttpClientTemplateTopicDto {

   private final String topicVariableName;
   private final RestConfiguration configuration;
   private final Set<TransportTopicDto> topics;

   /**
    * Constructor.
    * 
    * @param transportTopicConfigurationDto transport topic configuration dto
    */
   public HttpClientTemplateTopicDto(ITransportTopicConfigurationDto<
            RestConfigurationDto> transportTopicConfigurationDto) {
      this.topicVariableName = transportTopicConfigurationDto.getTopicVariableName();
      this.configuration = transportTopicConfigurationDto.getValue().getConfiguration();
      this.topics = transportTopicConfigurationDto.getTransportTopics();
   }

   public Set<TransportTopicDto> getTransportTopics() {
      return topics;
   }

   public String getVariableName() {
      return topicVariableName;
   }

   public String getNetworkAddress() {
      return configuration.getNetworkAddress().getAddress();
   }

   public String getPath() {
      return configuration.getPath();
   }

   public String getContentType() {
      return configuration.getContentType();
   }

   public HttpMethod getHttpMethod() {
      return configuration.getHttpMethod();
   }

   public int getPort() {
      return configuration.getPort();
   }

}
