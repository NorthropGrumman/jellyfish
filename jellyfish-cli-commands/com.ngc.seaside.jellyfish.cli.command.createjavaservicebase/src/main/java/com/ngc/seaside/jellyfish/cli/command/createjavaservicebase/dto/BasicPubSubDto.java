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
