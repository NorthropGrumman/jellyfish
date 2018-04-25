package com.ngc.seaside.jellyfish.cli.command.test.service;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IRequestResponseMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.jellyfish.service.scenario.api.ITimingConstraint;
import com.ngc.seaside.jellyfish.service.scenario.api.MessagingParadigm;
import com.ngc.seaside.jellyfish.service.scenario.impl.scenarioservice.ScenarioService;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.CorrelateStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.PublishStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.ReceiveRequestStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.ReceiveStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.RespondStepHandler;

import java.util.Collection;
import java.util.Optional;

import static org.mockito.Mockito.mock;

public class MockedScenarioService implements IScenarioService {

   private final ScenarioService delegate;

   /**
    * Default constructor
    */
   public MockedScenarioService() {
      delegate = new ScenarioService();
      delegate.setLogService(mock(ILogService.class));
      delegate.setCorrelationStepHandler(new CorrelateStepHandler());
      delegate.setPublishStepHandler(new PublishStepHandler());
      delegate.setReceiveStepHandler(new ReceiveStepHandler());
      delegate.setReceiveRequestStepHandler(new ReceiveRequestStepHandler());
      delegate.setRespondStepHandler(new RespondStepHandler());
      delegate.activate();
   }

   @Override
   public Collection<MessagingParadigm> getMessagingParadigms(IJellyFishCommandOptions options, IScenario scenario) {
      return delegate.getMessagingParadigms(options, scenario);
   }

   @Override
   public Optional<IPublishSubscribeMessagingFlow> getPubSubMessagingFlow(IJellyFishCommandOptions options,
                                                                          IScenario scenario) {
      return delegate.getPubSubMessagingFlow(options, scenario);
   }

   @Override
   public Optional<IRequestResponseMessagingFlow> getRequestResponseMessagingFlow(IJellyFishCommandOptions options,
                                                                                  IScenario scenario) {
      return delegate.getRequestResponseMessagingFlow(options, scenario);
   }

   @Override
   public Collection<ITimingConstraint> getTimingConstraints(IJellyFishCommandOptions options, IScenario scenario) {
      return delegate.getTimingConstraints(options, scenario);
   }

}
