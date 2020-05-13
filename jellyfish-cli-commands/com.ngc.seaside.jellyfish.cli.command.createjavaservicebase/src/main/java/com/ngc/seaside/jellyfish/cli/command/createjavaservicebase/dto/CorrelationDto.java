/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
