package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Default implementation of {@link ITransportTopicConfigurationDto}.
 */
public class DefaultTransportTopicConfigurationDto<T> implements ITransportTopicConfigurationDto<T> {

   private final T value;
   private final Set<TransportTopicDto> topics = new LinkedHashSet<>();

   public DefaultTransportTopicConfigurationDto(T value) {
      this.value = value;
   }

   public DefaultTransportTopicConfigurationDto<T> addTransportTopic(String type, String value) {
      return addTransportTopic(new TransportTopicDto(type, value));
   }
   
   public DefaultTransportTopicConfigurationDto<T> addTransportTopic(TransportTopicDto topic) {
      topics.add(topic);
      return this;
   }

   @Override
   public T getValue() {
      return value;
   }

   @Override
   public Set<TransportTopicDto> getTransportTopics() {
      return topics;
   }

}
