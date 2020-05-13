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
package com.ngc.seaside.jellyfish.service.scenario.impl.scenarioservice.processor;

import com.ngc.seaside.jellyfish.service.scenario.api.IRequestResponseMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.correlation.api.ICorrelationDescription;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.util.Optional;

public class ServerSideRequestResponseMessagingFlow implements IRequestResponseMessagingFlow {

   private IDataReferenceField input;

   private IDataReferenceField output;

   private IScenario scenario;

   @Override
   public FlowType getFlowType() {
      return FlowType.SERVER;
   }

   @Override
   public IDataReferenceField getInput() {
      return input;
   }

   public ServerSideRequestResponseMessagingFlow setInput(IDataReferenceField input) {
      this.input = input;
      return this;
   }

   @Override
   public IDataReferenceField getOutput() {
      return output;
   }

   public ServerSideRequestResponseMessagingFlow setOutput(IDataReferenceField output) {
      this.output = output;
      return this;
   }

   @Override
   public IScenario getScenario() {
      return scenario;
   }

   public ServerSideRequestResponseMessagingFlow setScenario(IScenario scenario) {
      this.scenario = scenario;
      return this;
   }

   @Override
   public Optional<IModelReferenceField> getInvokedServerSideComponent() {
      return Optional.empty();
   }

   @Override
   public Optional<IScenario> getInvokedServerSideScenario() {
      return Optional.empty();
   }


   @Override
   public Optional<ICorrelationDescription> getCorrelationDescription() {
      // Correlation is not support for request/response flows.
      return Optional.empty();
   }
}
