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

import com.ngc.seaside.jellyfish.service.scenario.api.IRequestResponseMessagingFlow;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.ReceiveRequestStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.RespondStepHandler;

import java.util.Optional;

public class RequestResponseProcessor {

   private final ReceiveRequestStepHandler receiveRequestStepHandler;
   private final RespondStepHandler respondStepHandler;

   public RequestResponseProcessor(ReceiveRequestStepHandler receiveRequestStepHandler,
                                   RespondStepHandler respondStepHandler) {
      this.receiveRequestStepHandler = receiveRequestStepHandler;
      this.respondStepHandler = respondStepHandler;
   }

   public boolean isRequestResponse(IScenario scenario) {
      return getFlow(scenario).isPresent();
   }

   /**
    * Parses a scenario to determine the messaging flow.  Please note the comment block in the method.
    *
    * @param scenario the scenario
    * @return an optional request response messaging flow
    */
   public Optional<IRequestResponseMessagingFlow> getFlow(IScenario scenario) {
      Optional<IRequestResponseMessagingFlow> flow = Optional.empty();

      // The scenario must contain both receiveRequest and respond steps.  Note the validation should not allow
      // declaring a scenario that has a receiveRequest step without a respond step but we check anyway.
      // Validation also requires there be only one receiveRequest and respond step per scenario.
      Optional<IScenarioStep> receiveRequestStep = scenario.getWhens()
            .stream()
            .filter(s -> ReceiveRequestStepHandler.PRESENT.getVerb().equals(s.getKeyword()))
            .findFirst();
      Optional<IScenarioStep> respondStep = scenario.getThens()
            .stream()
            .filter(s -> RespondStepHandler.FUTURE.getVerb().equals(s.getKeyword()))
            .findFirst();

      if (receiveRequestStep.isPresent() && respondStep.isPresent()) {
         flow = Optional.of(new ServerSideRequestResponseMessagingFlow()
                                  .setScenario(scenario)
                                  .setInput(receiveRequestStepHandler.getRequest(receiveRequestStep.get()))
                                  .setOutput(respondStepHandler.getResponse(respondStep.get())));
      }

      return flow;
   }
}
