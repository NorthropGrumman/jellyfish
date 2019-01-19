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

public class BasicPubSubDto {

   private String name;
   private InputDto input;
   private PublishDto output;
   private String serviceMethod;
   private String scenarioName;
   private List<IOCorrelationDto> inputOutputCorrelations = new ArrayList<>();

   public boolean isCorrelating() {
      return !inputOutputCorrelations.isEmpty();
   }

   public String getName() {
      return name;
   }

   public BasicPubSubDto setName(String name) {
      this.name = name;
      return this;
   }

   public InputDto getInput() {
      return input;
   }

   public BasicPubSubDto setInput(InputDto input) {
      this.input = input;
      return this;
   }

   public PublishDto getOutput() {
      return output;
   }

   public BasicPubSubDto setOutput(PublishDto output) {
      this.output = output;
      return this;
   }

   public String getServiceMethod() {
      return serviceMethod;
   }

   public BasicPubSubDto setServiceMethod(String serviceMethod) {
      this.serviceMethod = serviceMethod;
      return this;
   }

   public String getScenarioName() {
      return scenarioName;
   }

   public BasicPubSubDto setScenarioName(String scenarioName) {
      this.scenarioName = scenarioName;
      return this;
   }

   public List<IOCorrelationDto> getInputOutputCorrelations() {
      return inputOutputCorrelations;
   }

   public BasicPubSubDto setInputOutputCorrelations(List<IOCorrelationDto> inputOutputCorrelations) {
      this.inputOutputCorrelations = inputOutputCorrelations;
      return this;
   }

}
