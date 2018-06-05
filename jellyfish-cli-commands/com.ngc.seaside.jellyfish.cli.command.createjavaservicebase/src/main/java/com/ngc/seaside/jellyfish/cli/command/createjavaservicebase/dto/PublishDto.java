package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto;

public class PublishDto {

   private String type;
   private String finalizedType;
   private String name;
   private String topic;
   private String fieldName;
   private String fullyQualifiedName;
   private String correlationSetterSnippet;

   public String getType() {
      return type;
   }

   public PublishDto setType(String type) {
      this.type = type;
      return this;
   }

   public String getName() {
      return name;
   }

   public PublishDto setName(String name) {
      this.name = name;
      return this;
   }

   public String getFieldName() {
      return fieldName;
   }

   public PublishDto setFieldName(String fieldName) {
      this.fieldName = fieldName;
      return this;
   }

   public String getTopic() {
      return topic;
   }

   public PublishDto setTopic(String topic) {
      this.topic = topic;
      return this;
   }

   public PublishDto setFullyQualifiedName(String fullyQualifiedName) {
      this.fullyQualifiedName = fullyQualifiedName;
      return this;
   }

   public String getFullyQualifiedName() {
      return fullyQualifiedName;
   }

   public String getFinalizedType() {
      return finalizedType;
   }

   public PublishDto setFinalizedType(String finalizedType) {
      this.finalizedType = finalizedType;
      return this;
   }

   public String getCorrelationSetterSnippet() {
      return correlationSetterSnippet;
   }

   public PublishDto setCorrelationSetterSnippet(String correlationSetterSnippet) {
      this.correlationSetterSnippet = correlationSetterSnippet;
      return this;
   }
}
