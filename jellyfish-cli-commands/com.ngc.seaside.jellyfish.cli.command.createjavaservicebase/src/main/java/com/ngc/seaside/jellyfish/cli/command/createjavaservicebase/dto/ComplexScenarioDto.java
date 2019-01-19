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
 * Dto for dealing with scenarios with multiple pubsub inputs and/or outputs that don't have correlation
 */
public class ComplexScenarioDto {

   private String name;
   
   private String startMethod;
   
   private String serviceMethod;
   
   private List<InputDto> inputs = new ArrayList<>();
   
   private List<PublishDto> outputs = new ArrayList<>();

   public String getName() {
      return name;
   }

   public ComplexScenarioDto setName(String name) {
      this.name = name;
      return this;
   }

   public String getStartMethod() {
      return startMethod;
   }

   public ComplexScenarioDto setStartMethod(String startMethod) {
      this.startMethod = startMethod;
      return this;
   }

   public String getServiceMethod() {
      return serviceMethod;
   }

   public ComplexScenarioDto setServiceMethod(String serviceMethod) {
      this.serviceMethod = serviceMethod;
      return this;
   }

   public List<InputDto> getInputs() {
      return inputs;
   }

   public ComplexScenarioDto setInputs(List<InputDto> inputs) {
      this.inputs = inputs;
      return this;
   }

   public List<PublishDto> getOutputs() {
      return outputs;
   }

   public ComplexScenarioDto setOutputs(List<PublishDto> outputs) {
      this.outputs = outputs;
      return this;
   }
   
}
