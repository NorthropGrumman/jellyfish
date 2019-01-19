/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish.service.scenario.impl.scenarioservice;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IRequestResponseMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.jellyfish.service.scenario.api.ITimingConstraint;
import com.ngc.seaside.jellyfish.service.scenario.api.MessagingParadigm;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.CorrelateStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.PublishStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.ReceiveRequestStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.ReceiveStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.RespondStepHandler;

import java.util.Collection;
import java.util.Optional;

public class ScenarioServiceGuiceWrapper implements IScenarioService {

   private final ScenarioService scenarioService;

   @Inject
   public ScenarioServiceGuiceWrapper(ILogService logService,
                                      ReceiveStepHandler receiveStepHandler,
                                      PublishStepHandler publishStepHandler,
                                      CorrelateStepHandler correlationStepHandler,
                                      ReceiveRequestStepHandler receiveRequestStepHandler,
                                      RespondStepHandler respondStepHandler) {
      scenarioService = new ScenarioService();
      scenarioService.setLogService(logService);
      scenarioService.setPublishStepHandler(publishStepHandler);
      scenarioService.setReceiveStepHandler(receiveStepHandler);
      scenarioService.setCorrelationStepHandler(correlationStepHandler);
      scenarioService.setReceiveRequestStepHandler(receiveRequestStepHandler);
      scenarioService.setRespondStepHandler(respondStepHandler);
      scenarioService.activate();
   }

   @Override
   public Collection<MessagingParadigm> getMessagingParadigms(
         IJellyFishCommandOptions options,
         IScenario scenario) {
      return scenarioService.getMessagingParadigms(options, scenario);
   }

   @Override
   public Optional<IPublishSubscribeMessagingFlow> getPubSubMessagingFlow(
         IJellyFishCommandOptions options, IScenario scenario) {
      return scenarioService.getPubSubMessagingFlow(options, scenario);
   }

   @Override
   public Optional<IRequestResponseMessagingFlow> getRequestResponseMessagingFlow(
         IJellyFishCommandOptions options, IScenario scenario) {
      return scenarioService.getRequestResponseMessagingFlow(options, scenario);
   }

   @Override
   public Collection<ITimingConstraint> getTimingConstraints(
         IJellyFishCommandOptions options,
         IScenario scenario) {
      return scenarioService.getTimingConstraints(options, scenario);
   }
}
