package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.zeromq;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.AbstractTransportProviderConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.zeromq.ZeroMqTcpTemplateTopicDto.Socket;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationDto.TransportTopicDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.zeromq.ZeroMqTcpConfigurationDto;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ZeroMqTcpTemplateDto extends AbstractTransportProviderConfigurationDto {

   private static final String ZEROMQ_TOPIC =
            "com.ngc.seaside.service.transport.impl.topic.zeromq.ZeroMQTopic";
   private static final String CONFIGURATION_TYPE_SUFFIX = "ZeroMqConfiguration";
   private static final String ZEROMQ_PROVIDER_TARGET =
            "(component.name=com.ngc.seaside.service.transport.impl.provider.zeromq.ZeroMQTransportProvider)";
   private static final String ZEROMQ_MODULE =
            "com.ngc.seaside.service.transport.impl.provider.zeromq.module.ZeroMQTransportProviderModule";

   private Set<ZeroMqTcpTemplateTopicDto> topicDtos = new LinkedHashSet<>();

   public ZeroMqTcpTemplateDto(ConfigurationContext context) {
      super(context, ZEROMQ_TOPIC, CONFIGURATION_TYPE_SUFFIX, ZEROMQ_PROVIDER_TARGET, ZEROMQ_MODULE);
   }

   public String getPackageName() {
      return getBasePackage();
   }

   /**
    * Adds the given topic configuration dto.
    * 
    * @param topicDto topic configuration dto
    */
   public void addTopic(ITransportTopicConfigurationDto<ZeroMqTcpConfigurationDto> topicDto) {
      if (topicDto.getValue().canPublish()) {
         topicDtos.add(new ZeroMqTcpTemplateTopicDto(topicDto, Socket.PUBLISH));
         for (TransportTopicDto transportTopicDto : topicDto.getTransportTopics()) {
            getImports().add(transportTopicDto.getType());
         }
      }
      if (topicDto.getValue().canSubscribe()) {
         topicDtos.add(new ZeroMqTcpTemplateTopicDto(topicDto, Socket.SUBSCRIBE));
         for (TransportTopicDto transportTopicDto : topicDto.getTransportTopics()) {
            getImports().add(transportTopicDto.getType());
         }
      }
      if (topicDto.getValue().canRequest()) {
         topicDtos.add(new ZeroMqTcpTemplateTopicDto(topicDto, Socket.REQUEST));
         for (TransportTopicDto transportTopicDto : topicDto.getTransportTopics()) {
            getImports().add(transportTopicDto.getType());
         }
      }
      if (topicDto.getValue().canRespond()) {
         topicDtos.add(new ZeroMqTcpTemplateTopicDto(topicDto, Socket.RESPOND));
         for (TransportTopicDto transportTopicDto : topicDto.getTransportTopics()) {
            getImports().add(transportTopicDto.getType());
         }
      }
   }

   public Map<String, Set<ZeroMqTcpTemplateTopicDto>> getSendTopics() {
      return getTopics(true);
   }

   public Map<String, Set<ZeroMqTcpTemplateTopicDto>> getReceiveTopics() {
      return getTopics(false);
   }

   private Map<String, Set<ZeroMqTcpTemplateTopicDto>> getTopics(boolean send) {
      Map<String, Set<ZeroMqTcpTemplateTopicDto>> map = new LinkedHashMap<>();
      for (ZeroMqTcpTemplateTopicDto topicDto : topicDtos) {
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

   public Set<ZeroMqTcpTemplateTopicDto> getTopics() {
      return topicDtos;
   }

}
