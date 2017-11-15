package com.ngc.seaside.jellyfish.service.codegen.api.dto;

import com.ngc.seaside.jellyfish.service.scenario.correlation.api.ICorrelationExpression;

import java.util.Collection;
import java.util.Map;

/**
 * Represents the description of the pub/sub method
 */
public class PubSubMethodDto extends MethodDto {

   private Map<String, ? extends MethodDto> publishMethods;
   private String publishingTopic;
   private Collection<ICorrelationExpression> inputInputCorrelations;
   private Collection<ICorrelationExpression> inputOutputCorrelations;

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
   
   public Collection<ICorrelationExpression> getInputInputCorrelations() {
      return inputInputCorrelations;
   }
   
   public Collection<ICorrelationExpression> getInputOutputCorrelations() {
      return inputOutputCorrelations;
   }

   public PubSubMethodDto setInputInputCorrelations(Collection<ICorrelationExpression> inputInputCorrelations) {
      this.inputInputCorrelations = inputInputCorrelations;
      return this;
   }

   public PubSubMethodDto setInputOutputCorrelations(Collection<ICorrelationExpression> inputOutputCorrelations) {
      this.inputOutputCorrelations = inputOutputCorrelations;
      return this;
   }
   
   public boolean hasCorrelation() {
      if (!inputInputCorrelations.isEmpty() ||  !inputOutputCorrelations.isEmpty()) {
         return true;
      } else {
         return false;
      }
   }
}
