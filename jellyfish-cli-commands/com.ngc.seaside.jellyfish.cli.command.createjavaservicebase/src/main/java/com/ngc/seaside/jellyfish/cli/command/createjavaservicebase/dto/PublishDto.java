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
