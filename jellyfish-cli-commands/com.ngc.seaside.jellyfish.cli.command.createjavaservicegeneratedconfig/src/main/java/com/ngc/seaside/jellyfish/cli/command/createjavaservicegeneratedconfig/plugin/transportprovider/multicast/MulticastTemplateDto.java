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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.multicast;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.AbstractTransportProviderConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationDto.TransportTopicDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.multicast.MulticastConfigurationDto;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class MulticastTemplateDto extends AbstractTransportProviderConfigurationDto {

   private static final String MULTICAST_TOPIC =
            "com.ngc.seaside.service.transport.impl.topic.multicast.MulticastTopic";
   private static final String CONFIGURATION_TYPE_SUFFIX = "MulticastConfiguration";
   private static final String MULTICAST_PROVIDER_TARGET =
            "(component.name=com.ngc.seaside.service.transport.impl.provider.multicast.MulticastTransportProvider)";
   private static final String MULTICAST_MODULE =
            "com.ngc.seaside.service.transport.impl.provider.multicast.module.MulticastTransportProviderModule";

   private Set<MulticastTemplateTopicDto> topicDtos = new LinkedHashSet<>();

   public MulticastTemplateDto(ConfigurationContext context) {
      super(context, MULTICAST_TOPIC, CONFIGURATION_TYPE_SUFFIX, MULTICAST_PROVIDER_TARGET, MULTICAST_MODULE);
   }

   public String getPackageName() {
      return getBasePackage();
   }

   /**
    * Adds the given topic configuration dto.
    * 
    * @param topicDto topic configuration dto
    */
   public void addTopic(ITransportTopicConfigurationDto<MulticastConfigurationDto> topicDto) {
      if (topicDto.getValue().canSend()) {
         topicDtos.add(new MulticastTemplateTopicDto(topicDto, true));
         for (TransportTopicDto transportTopicDto : topicDto.getTransportTopics()) {
            getImports().add(transportTopicDto.getType());
         }
      }
      if (topicDto.getValue().canReceive()) {
         topicDtos.add(new MulticastTemplateTopicDto(topicDto, false));
         for (TransportTopicDto transportTopicDto : topicDto.getTransportTopics()) {
            getImports().add(transportTopicDto.getType());
         }
      }
   }

   public Map<String, Set<MulticastTemplateTopicDto>> getSendTopics() {
      return getTopics(true);
   }

   public Map<String, Set<MulticastTemplateTopicDto>> getReceiveTopics() {
      return getTopics(false);
   }

   private Map<String, Set<MulticastTemplateTopicDto>> getTopics(boolean send) {
      Map<String, Set<MulticastTemplateTopicDto>> map = new LinkedHashMap<>();
      for (MulticastTemplateTopicDto topicDto : topicDtos) {
         if (send != topicDto.isSend()) {
            continue;
         }
         for (TransportTopicDto transportTopicDto : topicDto.getTransportTopics()) {
            String topicType = transportTopicDto.getType();
            String topic = topicType.substring(topicType.lastIndexOf('.') + 1) + transportTopicDto.getValue();
            map.computeIfAbsent(topic, __ -> new LinkedHashSet<>()).add(topicDto);
         }
      }
      return map;
   }

   public Set<MulticastTemplateTopicDto> getTopics() {
      return topicDtos;
   }

}
