package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto2;

import java.util.List;

public class CorrelationDto {
   private String name;
   private String outputType;
   private String serviceName;
   private String correlationType;
   private String inputLogFormat;
   private String publishMethod;
   private List<InputDto> inputs;
   private List<IOCorrelationDto> inputOutputCorrelations;

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getOutputType() {
      return outputType;
   }

   public void setOutputType(String outputType) {
      this.outputType = outputType;
   }

   public String getServiceName() {
      return serviceName;
   }

   public void setServiceName(String serviceName) {
      this.serviceName = serviceName;
   }

   public String getCorrelationType() {
      return correlationType;
   }

   public void setCorrelationType(String correlationType) {
      this.correlationType = correlationType;
   }

   public String getInputLogFormat() {
      return inputLogFormat;
   }

   public void setInputLogFormat(String inputLogFormat) {
      this.inputLogFormat = inputLogFormat;
   }

   public String getPublishMethod() {
      return publishMethod;
   }

   public void setPublishMethod(String publishMethod) {
      this.publishMethod = publishMethod;
   }

   public List<InputDto> getInputs() {
      return inputs;
   }

   public void setInputs(List<InputDto> inputs) {
      this.inputs = inputs;
   }

   public List<IOCorrelationDto> getInputOutputCorrelations() {
      return inputOutputCorrelations;
   }

   public void setInputOutputCorrelations(List<IOCorrelationDto> inputOutputCorrelations) {
      this.inputOutputCorrelations = inputOutputCorrelations;
   }

   public static class IOCorrelationDto {
      private String getterSnippet;
      private String setterSnippet;
      private String inputType;

      public String getGetterSnippet() {
         return getterSnippet;
      }

      public IOCorrelationDto setGetterSnippet(String getterSnippet) {
         this.getterSnippet = getterSnippet;
         return this;
      }

      public String getSetterSnippet() {
         return setterSnippet;
      }

      public IOCorrelationDto setSetterSnippet(String setterSnippet) {
         this.setterSnippet = setterSnippet;
         return this;
      }

      public String getInputType() {
         return inputType;
      }

      public IOCorrelationDto setInputType(String inputType) {
         this.inputType = inputType;
         return this;
      }

   }
}
