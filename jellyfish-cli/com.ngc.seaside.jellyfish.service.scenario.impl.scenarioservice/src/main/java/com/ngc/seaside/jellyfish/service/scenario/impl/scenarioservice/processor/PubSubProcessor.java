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

import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow.FlowType;
import com.ngc.seaside.jellyfish.service.scenario.correlation.api.ICorrelationDescription;
import com.ngc.seaside.jellyfish.service.scenario.impl.scenarioservice.correlation.CorrelationDescription;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.CorrelateStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.PublishStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.ReceiveStepHandler;

import java.util.Optional;

/**
 * Handles the discovery of pub/sub flows of scenarios.
 */
public class PubSubProcessor {

   private final PublishStepHandler publishStepHandler;
   private final ReceiveStepHandler receiveStepHandler;
   private final CorrelateStepHandler correlateStepHandler;

   /**
    * Constructor.
    *
    * @param publishStepHandler   the publish step handler
    * @param receiveStepHandler   the receive step handler
    * @param correlateStepHandler the correlate step handler
    */
   public PubSubProcessor(PublishStepHandler publishStepHandler,
                          ReceiveStepHandler receiveStepHandler,
                          CorrelateStepHandler correlateStepHandler) {
      this.publishStepHandler = publishStepHandler;
      this.receiveStepHandler = receiveStepHandler;
      this.correlateStepHandler = correlateStepHandler;
   }

   public boolean isPublishSubscribe(IScenario scenario) {
      return getFlow(scenario).isPresent();
   }

   /**
    * Parses a scenario to determine the messaging flow.  The flows inputs and outputs are populated given the
    * current state of this class.
    *
    * @param scenario the scenario
    * @return an optional pubsub messaging flow
    */
   public Optional<IPublishSubscribeMessagingFlow> getFlow(IScenario scenario) {
      boolean receive = false;
      boolean publish = false;

      for (IScenarioStep step : scenario.getWhens()) {
         if (ReceiveStepHandler.PRESENT.getVerb().equals(step.getKeyword())) {
            receive = true;
         }
      }

      for (IScenarioStep step : scenario.getThens()) {
         if (PublishStepHandler.FUTURE.getVerb().equals(step.getKeyword())) {
            publish = true;
         }
      }

      final PublishSubscribeMessagingFlow flow;
      if (receive && publish) {
         flow = new PublishSubscribeMessagingFlow(FlowType.PATH);
      } else if (!receive && publish) {
         flow = new PublishSubscribeMessagingFlow(FlowType.SOURCE);
      } else if (receive && !publish) {
         flow = new PublishSubscribeMessagingFlow(FlowType.SINK);
      } else {
         return Optional.empty();
      }

      for (IScenarioStep step : scenario.getWhens()) {
         if (ReceiveStepHandler.PRESENT.getVerb().equals(step.getKeyword())) {
            flow.getInputsModifiable().add(receiveStepHandler.getInputs(step));
         }
      }

      for (IScenarioStep step : scenario.getThens()) {
         if (PublishStepHandler.FUTURE.getVerb().equals(step.getKeyword())) {
            flow.getOutputsModifiable().add(publishStepHandler.getOutputs(step));
         }
      }

      flow.setCorrelationDescriptor(getCorrelationDescription(scenario));
      flow.setScenario(scenario);
      return Optional.of(flow);
   }

   private ICorrelationDescription getCorrelationDescription(IScenario scenario) {
      ICorrelationDescription correlationDescription = null;
      if (!scenario.getSteps(CorrelateStepHandler.PRESENT.getVerb(), CorrelateStepHandler.FUTURE.getVerb()).isEmpty()) {
         correlationDescription = new CorrelationDescription(scenario, correlateStepHandler);
      }
      return correlationDescription;
   }
}
