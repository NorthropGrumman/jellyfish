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

import java.util.ArrayList;
import java.util.List;

public class ReceiveDto {

   private String topic;
   private String name;
   private String eventType;
   private List<String> basicScenarios = new ArrayList<>();
   private boolean hasCorrelations;

   public String getTopic() {
      return topic;
   }

   public ReceiveDto setTopic(String topic) {
      this.topic = topic;
      return this;
   }

   public String getName() {
      return name;
   }

   public ReceiveDto setName(String name) {
      this.name = name;
      return this;
   }

   public String getEventType() {
      return eventType;
   }

   public ReceiveDto setEventType(String eventType) {
      this.eventType = eventType;
      return this;
   }

   public List<String> getBasicScenarios() {
      return basicScenarios;
   }

   public ReceiveDto setBasicScenarios(List<String> basicScenarios) {
      this.basicScenarios = basicScenarios;
      return this;
   }

   public boolean hasCorrelations() {
      return hasCorrelations;
   }

   public ReceiveDto setHasCorrelations(boolean hasCorrelations) {
      this.hasCorrelations = hasCorrelations;
      return this;
   }

}
