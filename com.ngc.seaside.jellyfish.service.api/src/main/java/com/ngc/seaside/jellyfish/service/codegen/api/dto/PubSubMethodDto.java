package com.ngc.seaside.jellyfish.service.codegen.api.dto;

import java.util.Map;

/**
 * Represents the description of the pub/sub method
 */
public class PubSubMethodDto extends MethodDto {

   private Map<String, ? extends MethodDto> publishMethods;
   private String publishingTopic;

   /**
    * Gets whether or not this method is associated with publishing. This will also return true for sources and false for sinks.
    */
   public boolean isPublisher() {
      return publishingTopic != null;
   }

   /**
    * If this method is a subscriber, returns the set of scenario method names with the corresponding publishing method. The publishing method will be null for sinks/sources.
    */
   public Map<String, ? extends MethodDto> getPublishMethods() {
      return publishMethods;
   }

   public PubSubMethodDto setPublishMethods(Map<String, ? extends MethodDto> publishMethods) {
      this.publishMethods = publishMethods;
      return this;
   }

   /**
    * If this method is a publisher or source, returns the topic to publish to; otherwise, returns false.
    */
   public String getPublishingTopic() {
      return publishingTopic;
   }

   public PubSubMethodDto setPublishingTopic(String topic) {
      this.publishingTopic = topic;
      return this;
   }

}
