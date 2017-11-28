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
