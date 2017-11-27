package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto;

import java.util.List;

/**
 * A dto for generating that method that handles the logic when the correlation expressions for a scenario are completed. This involves calling the corresponding service interface method, catching any
 * service fault exceptions from it, logging the inputs and outputs, and publishing the outputs.
 */
public class CorrelationDto {
   private String name;
   private String outputType;
   private String serviceName;
   private String scenarioName;
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
