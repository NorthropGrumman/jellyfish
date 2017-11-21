package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto2;

import java.util.List;

/**
 * A dto for generating that method that handles the logic when the correlation expressions for a scenario are completed. This involves calling the corresponding service interface method, catching any
 * service fault exceptions from it, logging the inputs and outputs, and publishing the outputs.
 */
public class CorrelationDto {
   private String name;
   private String outputType;
   private String serviceName;
   private String correlationType;
   private String inputLogFormat;
   private String publishMethod;
   private List<InputDto> inputs;
   private List<IOCorrelationDto> inputOutputCorrelations;

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
   public String getOutputType() {
      return outputType;
   }

   public void setOutputType(String outputType) {
      this.outputType = outputType;
   }

   /**
    * Returns the name of the method for the service interface that the method for this dto will call.
    */
   public String getServiceName() {
      return serviceName;
   }

   public void setServiceName(String serviceName) {
      this.serviceName = serviceName;
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
    * Returns the method that should be called to publish the scenario's output.
    */
   public String getPublishMethod() {
      return publishMethod;
   }

   public void setPublishMethod(String publishMethod) {
      this.publishMethod = publishMethod;
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

   public static class IOCorrelationDto {
      private String getterSnippet;
      private String setterSnippet;
      private String inputType;

      /**
       * Returns the snippet of code used to get the input field that is being correlated. This typically is a chain of getter methods.
       */
      public String getGetterSnippet() {
         return getterSnippet;
      }

      public IOCorrelationDto setGetterSnippet(String getterSnippet) {
         this.getterSnippet = getterSnippet;
         return this;
      }

      /**
       * Returns the snippet of code used to set the output field that is being correlated. This typically is a chain of getter methods following by the name of the setter method.
       */
      public String getSetterSnippet() {
         return setterSnippet;
      }

      public IOCorrelationDto setSetterSnippet(String setterSnippet) {
         this.setterSnippet = setterSnippet;
         return this;
      }

      /**
       * Returns the generated java events type for the input.
       */
      public String getInputType() {
         return inputType;
      }

      public IOCorrelationDto setInputType(String inputType) {
         this.inputType = inputType;
         return this;
      }

   }
}
