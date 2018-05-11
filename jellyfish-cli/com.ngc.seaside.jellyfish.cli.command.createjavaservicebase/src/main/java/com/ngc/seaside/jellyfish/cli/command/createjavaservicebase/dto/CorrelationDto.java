package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * A dto for generating that method that handles the logic when the correlation expressions for a
 * scenario are completed. This involves calling the corresponding service interface method, catching any
 * service fault exceptions from it, logging the inputs and outputs, and publishing the outputs.
 */
public class CorrelationDto {

   private String name;
   private PublishDto output;
   private String serviceMethod;
   private String serviceTryMethodSnippet;
   private String serviceRegisterSnippet;
   private String scenarioName;
   private String correlationType;
   private String inputLogFormat;
   private String inputClassListSnippet;
   private String serviceFromStatusSnippet;
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

   public String getServiceTryMethodSnippet() {
      return serviceTryMethodSnippet;
   }

   public void setServiceTryMethodSnippet(String serviceName) {
      this.serviceTryMethodSnippet = "try" + StringUtils.capitalize(serviceName);
   }

   public String getServiceRegisterSnippet() {
      return serviceRegisterSnippet;
   }

   public void setServiceRegisterSnippet(String serviceName) {
      this.serviceRegisterSnippet = "register" + StringUtils.capitalize(serviceName) + "Trigger()";
   }

   public String getInputClassListSnippet() {
      String formattedClassList = "";
      for (InputDto inputClass : inputs) {
         formattedClassList += inputClass.getType() + ".class, ";
      }
      return formattedClassList;
   }

   public void setInputClassListSnippet(String inputClassListSnippet) {
      this.inputClassListSnippet = inputClassListSnippet;
   }

   public String getServiceFromStatusSnippet() {
      return serviceFromStatusSnippet;
   }

   public void setServiceFromStatusSnippet(String serviceName) {
      this.serviceFromStatusSnippet = serviceName + "FromStatus";
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
