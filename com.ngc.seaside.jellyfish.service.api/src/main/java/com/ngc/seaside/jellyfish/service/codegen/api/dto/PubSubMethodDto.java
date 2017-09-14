package com.ngc.seaside.jellyfish.service.codegen.api.dto;

import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow.FlowType;

import java.util.Map;

/**
 * Represents the description of the pub/sub method
 */
public class PubSubMethodDto extends MethodDto {

   private Map<String, ? extends MethodDto> publishMethods;
   private String publishingTopic;
   private IPublishSubscribeMessagingFlow flow;

   /**
    * Gets the pub/sub method's signature. Two pub/sub methods can be considered equal if their signatures are the same.
    */
   public String getPubSubMethodSignature() {
      return flow.getFlowType() + ":" + super.getMethodSignature();
   }

   /**
    * Gets the flow associated with this method.
    */
   public IPublishSubscribeMessagingFlow getFlow() {
      return flow;
   }

   public PubSubMethodDto setFlow(IPublishSubscribeMessagingFlow flow) {
      this.flow = flow;
      return this;
   }

   /**
    * Gets whether or not this method is associated with publishing
    */
   public boolean isPublisher() {
      if (flow.getFlowType() == FlowType.SOURCE) {
         return true;
      }
      if (flow.getFlowType() == FlowType.SINK) {
         return false;
      }
      return publishingTopic != null;
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
