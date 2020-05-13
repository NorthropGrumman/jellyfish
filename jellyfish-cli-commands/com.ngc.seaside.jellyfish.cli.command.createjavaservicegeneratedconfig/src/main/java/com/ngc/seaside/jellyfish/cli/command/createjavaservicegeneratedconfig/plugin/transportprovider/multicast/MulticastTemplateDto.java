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
