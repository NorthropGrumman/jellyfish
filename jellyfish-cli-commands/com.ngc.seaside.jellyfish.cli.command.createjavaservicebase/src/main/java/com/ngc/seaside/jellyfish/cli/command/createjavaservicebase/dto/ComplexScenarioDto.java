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
