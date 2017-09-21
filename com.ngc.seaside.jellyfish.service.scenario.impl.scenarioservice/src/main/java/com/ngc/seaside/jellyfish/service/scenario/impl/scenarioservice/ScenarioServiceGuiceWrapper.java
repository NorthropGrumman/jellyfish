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
import com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario.PublishStepHandler;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario.ReceiveStepHandler;

import java.util.Collection;

public class ScenarioServiceGuiceWrapper implements IScenarioService {

   private final ScenarioService scenarioService;

   @Inject
   public ScenarioServiceGuiceWrapper(ILogService logService,
                                      ReceiveStepHandler receiveStepHandler,
                                      PublishStepHandler publishStepHandler) {
      scenarioService = new ScenarioService();
      scenarioService.setLogService(logService);
      scenarioService.setPublishStepHandler(publishStepHandler);
      scenarioService.setReceiveStepHandler(receiveStepHandler);
      scenarioService.activate();
   }

   @Override
   public Collection<MessagingParadigm> getMessagingParadigms(
         IJellyFishCommandOptions options,
         IScenario scenario) {
      return scenarioService.getMessagingParadigms(options, scenario);
   }

   @Override
   public Collection<IPublishSubscribeMessagingFlow> getPubSubMessagingFlows(
         IJellyFishCommandOptions options, IScenario scenario) {
      return scenarioService.getPubSubMessagingFlows(options, scenario);
   }

   @Override
   public Collection<IRequestResponseMessagingFlow> getRequestResponseMessagingFlows(
         IJellyFishCommandOptions options, IScenario scenario) {
      return scenarioService.getRequestResponseMessagingFlows(options, scenario);
   }

   @Override
   public Collection<ITimingConstraint> getTimingConstraints(
         IJellyFishCommandOptions options,
         IScenario scenario) {
      return scenarioService.getTimingConstraints(options, scenario);
   }
}
