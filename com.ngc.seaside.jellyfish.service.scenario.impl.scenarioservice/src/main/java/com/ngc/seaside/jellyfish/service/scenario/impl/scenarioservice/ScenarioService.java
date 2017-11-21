package com.ngc.seaside.jellyfish.service.scenario.impl.scenarioservice;

import com.google.common.base.Preconditions;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IRequestResponseMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.jellyfish.service.scenario.api.ITimingConstraint;
import com.ngc.seaside.jellyfish.service.scenario.api.MessagingParadigm;
import com.ngc.seaside.jellyfish.service.scenario.impl.scenarioservice.processor.PubSubProcessor;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.scenario.api.IScenarioStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.CorrelateStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.PublishStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.ReceiveStepHandler;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

/**
 * An implementation of {@code IScenarioService} that mostly delegates to to components that handle specific messaging
 * paradigms.
 */
@Component(service = IScenarioService.class)
public class ScenarioService implements IScenarioService {

   private PubSubProcessor pubSubProcessor;

   private ReceiveStepHandler receiveStepHandler;
   private PublishStepHandler publishStepHandler;
   private CorrelateStepHandler correlateStepHandler;
   private ILogService logService;

   @Override
   public Collection<MessagingParadigm> getMessagingParadigms(IJellyFishCommandOptions options, IScenario scenario) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(scenario, "scenario may not be null!");

      Set<MessagingParadigm> paradigms = EnumSet.noneOf(MessagingParadigm.class);
      if (pubSubProcessor.isPublishSubscribe(scenario)) {
         paradigms.add(MessagingParadigm.PUBLISH_SUBSCRIBE);
      }
      return Collections.unmodifiableCollection(paradigms);
   }

   @Override
   public Collection<IPublishSubscribeMessagingFlow> getPubSubMessagingFlows(IJellyFishCommandOptions options,
                                                                             IScenario scenario) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(scenario, "scenario may not be null!");

      Collection<IPublishSubscribeMessagingFlow> flows = new ArrayList<>();
      flows.addAll(pubSubProcessor.getFlows(scenario));
      return Collections.unmodifiableCollection(flows);
   }
   
   @Override
   public Optional<IPublishSubscribeMessagingFlow> getPubSubMessagingFlow(IJellyFishCommandOptions options,
                                                                             IScenario scenario) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(scenario, "scenario may not be null!");

      Collection<IPublishSubscribeMessagingFlow> flows = new ArrayList<>();
      flows.addAll(pubSubProcessor.getFlows(scenario));
      if (flows.size() > 1) {
         throw new IllegalStateException("Received multiple publish/subscribe messaging flows for scenario " + scenario.getName());
      }
      if (flows.isEmpty()) {
         return Optional.empty();
      }
      return Optional.of(flows.iterator().next());
   }

   @Override
   public Collection<IRequestResponseMessagingFlow> getRequestResponseMessagingFlows(IJellyFishCommandOptions options,
                                                                                     IScenario scenario) {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public Collection<ITimingConstraint> getTimingConstraints(IJellyFishCommandOptions options, IScenario scenario) {
      // Note this logic is the same regardless of messaging paradigm.
      throw new UnsupportedOperationException("not implemented");
   }

   @Activate
   public void activate() {
      if (this.publishStepHandler != null && this.receiveStepHandler != null && this.correlateStepHandler != null) {
         this.pubSubProcessor = new PubSubProcessor(this.publishStepHandler, this.receiveStepHandler, this.correlateStepHandler);
      }
      logService.debug(getClass(), "activated");
   }

   @Deactivate
   public void deactivate() {
      logService.debug(getClass(), "deactivated");
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC)
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         target = "(component.name=com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps)")
   public void setReceiveStepHandler(IScenarioStepHandler ref) {
      this.receiveStepHandler = (ReceiveStepHandler) ref;
      if (this.publishStepHandler != null && this.receiveStepHandler != null && this.correlateStepHandler != null) {
         this.pubSubProcessor = new PubSubProcessor(this.publishStepHandler, this.receiveStepHandler, this.correlateStepHandler);
      }
   }

   public void removeReceiveStepHandler(IScenarioStepHandler ref) {
      this.receiveStepHandler = null;
      this.pubSubProcessor = null;
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         target = "(component.name=com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps)")
   public void setPublishStepHandler(IScenarioStepHandler ref) {
      this.publishStepHandler = (PublishStepHandler) ref;
      if (this.publishStepHandler != null && this.receiveStepHandler != null && this.correlateStepHandler != null) {
         this.pubSubProcessor = new PubSubProcessor(this.publishStepHandler, this.receiveStepHandler, this.correlateStepHandler);
      }
   }

   public void removePublishStepHandler(IScenarioStepHandler ref) {
      this.publishStepHandler = null;
      this.pubSubProcessor = null;
   }
   
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            target = "(component.name=com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps)")
      public void setCorrelationStepHandler(IScenarioStepHandler ref) {
         this.correlateStepHandler = (CorrelateStepHandler) ref;
         if (this.publishStepHandler != null && this.receiveStepHandler != null && this.correlateStepHandler != null) {
            this.pubSubProcessor = new PubSubProcessor(this.publishStepHandler, this.receiveStepHandler, this.correlateStepHandler);
         }
      }

      public void removeCorrelationStepHandler(IScenarioStepHandler ref) {
         this.publishStepHandler = null;
         this.pubSubProcessor = null;
      }
}
