/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
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
      if (scenario.getSteps(CorrelateStepHandler.PRESENT.getVerb(), CorrelateStepHandler.FUTURE.getVerb()).isEmpty()) {
         return null;
      }
      return new CorrelationDescription(scenario, correlateStepHandler);
   }
}
