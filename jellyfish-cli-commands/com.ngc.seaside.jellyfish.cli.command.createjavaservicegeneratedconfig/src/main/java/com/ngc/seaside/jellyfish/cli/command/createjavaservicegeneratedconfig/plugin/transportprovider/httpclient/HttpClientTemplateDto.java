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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.httpclient;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.AbstractTransportProviderConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationDto.TransportTopicDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.rest.RestConfigurationDto;

import java.util.LinkedHashSet;
import java.util.Set;

public class HttpClientTemplateDto extends AbstractTransportProviderConfigurationDto {

   private static final String HTTP_CLIENT_TOPIC =
            "com.ngc.seaside.service.transport.impl.topic.httpclient.HttpClientTopic";
   private static final String CONFIGURATION_TYPE_SUFFIX = "HttpClientConfiguration";
   private static final String HTTP_CLIENT_PROVIDER_TARGET =
            "(component.name=com.ngc.seaside.service.transport.impl.provider.httpclient.HttpClientTransportProvider)";
   private static final String HTTP_CLIENT_MODULE =
            "com.ngc.seaside.service.transport.impl.provider.httpclient.module.HttpClientTransportProviderModule";

   private Set<HttpClientTemplateTopicDto> topicDtos = new LinkedHashSet<>();

   public HttpClientTemplateDto(ConfigurationContext context) {
      super(context, HTTP_CLIENT_TOPIC, CONFIGURATION_TYPE_SUFFIX, HTTP_CLIENT_PROVIDER_TARGET, HTTP_CLIENT_MODULE);
   }

   public String getPackageName() {
      return getBasePackage();
   }

   /**
    * Adds the given topic configuration dto.
    * 
    * @param topicDto topic configuration dto
    */
   public void addTopic(ITransportTopicConfigurationDto<RestConfigurationDto> topicDto) {
      if (topicDto.getValue().canSend()) {
         topicDtos.add(new HttpClientTemplateTopicDto(topicDto));
         for (TransportTopicDto transportTopicDto : topicDto.getTransportTopics()) {
            getImports().add(transportTopicDto.getType());
         }
      }
   }

   public Set<HttpClientTemplateTopicDto> getTopics() {
      return topicDtos;
   }

}
