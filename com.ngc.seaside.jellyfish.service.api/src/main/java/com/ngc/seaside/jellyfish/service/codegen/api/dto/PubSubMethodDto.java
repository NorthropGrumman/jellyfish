package com.ngc.seaside.jellyfish.service.codegen.api.dto;

import java.util.Map;

/**
 * Represents the description of the pub/sub method
 */
public class PubSubMethodDto extends MethodDto {

   private Map<String, ? extends MethodDto> publishMethods;
   private String publishingTopic;
   private boolean isPublisher;

   /**
    * Gets whether or not this method is associated with publishing
    */
   public boolean isPublisher() {
      return isPublisher;
   }

   public PubSubMethodDto setPublisher(boolean publisher) {
      this.isPublisher = publisher;
      return this;
   }

   /**
    * If this method is a subscriber, returns the set of scenario method names with the corresponding publishing method.
    */
   public Map<String, ? extends MethodDto> getPublishMethods() {
      return publishMethods;
   }

   public PubSubMethodDto setPublishMethods(Map<String, ? extends MethodDto> publishMethods) {
      this.publishMethods = publishMethods;
      return this;
   }

   /**
    * If this method is a publisher, returns the topic to publish to.
    */
   public String getPublishingTopic() {
      return publishingTopic;
   }

   public PubSubMethodDto setPublishingTopic(String topic) {
      this.publishingTopic = topic;
      return this;
   }

}
