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

/**
 * A dto for generating that method that handles the logic when the correlation expressions for a
 * scenario are completed. This involves calling the corresponding service interface method, catching any
 * service fault exceptions from it, logging the inputs and outputs, and publishing the outputs.
 */
public class CorrelationDto {

   private String name;
   private PublishDto output;
   private String serviceMethod;
   private String serviceTryMethod;
   private String serviceTriggerRegister;
   private String scenarioName;
   private String correlationType;
   private String inputLogFormat;
   private String serviceFromStatus;
   private List<InputDto> inputs = new ArrayList<>();
   private List<IOCorrelationDto> inputOutputCorrelations = new ArrayList<>();

   /**
    * Returns the name of the method for this dto. The method is called when its correlations are completed.
    */
   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   /**
    * Returns the generated java events type of the output for the scenario this dto refers to.
    */
   public PublishDto getOutput() {
      return output;
   }

   public void setOutput(PublishDto output) {
      this.output = output;
   }

   /**
    * Returns the name of the method for the service interface that the method for this dto will call.
    */
   public String getServiceMethod() {
      return serviceMethod;
   }

   public void setServiceMethod(String serviceName) {
      this.serviceMethod = serviceName;
   }

   public String getServiceTryMethod() {
      return serviceTryMethod;
   }

   public void setServiceTryMethod(String serviceTryMethod) {
      this.serviceTryMethod = serviceTryMethod;
   }

   public String getServiceTriggerRegister() {
      return serviceTriggerRegister;
   }

   public void setServiceTriggerRegister(String serviceTriggerRegister) {
      this.serviceTriggerRegister = serviceTriggerRegister;
   }

   public String getServiceFromStatus() {
      return serviceFromStatus;
   }

   public void setServiceFromStatus(String serviceFromStatus) {
      this.serviceFromStatus = serviceFromStatus;
   }

   /**
    * Returns the generated java events type being correlated for this dto.
    */
   public String getCorrelationType() {
      return correlationType;
   }

   public void setCorrelationType(String correlationType) {
      this.correlationType = correlationType;
   }

   /**
    * Returns the input log format used for logging the inputs to the method for this dto.
    */
   public String getInputLogFormat() {
      return inputLogFormat;
   }

   public void setInputLogFormat(String inputLogFormat) {
      this.inputLogFormat = inputLogFormat;
   }

   /**
    * Returns the list of inputs to this scenario.
    */
   public List<InputDto> getInputs() {
      return inputs;
   }

   public void setInputs(List<InputDto> inputs) {
      this.inputs = inputs;
   }

   /**
    * Returns the list of input-output correlations for the scenario.
    */
   public List<IOCorrelationDto> getInputOutputCorrelations() {
      return inputOutputCorrelations;
   }

   public void setInputOutputCorrelations(List<IOCorrelationDto> inputOutputCorrelations) {
      this.inputOutputCorrelations = inputOutputCorrelations;
   }

   /**
    * Returns the scenario name
    */
   public String getScenarioName() {
      return scenarioName;
   }

   public void setScenarioName(String scenarioName) {
      this.scenarioName = scenarioName;
   }
}
