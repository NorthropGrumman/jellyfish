package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto;

public class BasicServerReqResDto {

   private String name;
   private InputDto input;
   private PublishDto output;
   private String serviceMethod;
   private String scenarioName;

   public String getName() {
      return name;
   }

   public BasicServerReqResDto setName(String name) {
      this.name = name;
      return this;
   }

   public InputDto getInput() {
      return input;
   }

   public BasicServerReqResDto setInput(InputDto input) {
      this.input = input;
      return this;
   }

   public PublishDto getOutput() {
      return output;
   }

   public BasicServerReqResDto setOutput(PublishDto output) {
      this.output = output;
      return this;
   }

   public String getServiceMethod() {
      return serviceMethod;
   }

   public BasicServerReqResDto setServiceMethod(String serviceMethod) {
      this.serviceMethod = serviceMethod;
      return this;
   }

   public String getScenarioName() {
      return scenarioName;
   }

   public BasicServerReqResDto setScenarioName(String scenarioName) {
      this.scenarioName = scenarioName;
      return this;
   }
}
