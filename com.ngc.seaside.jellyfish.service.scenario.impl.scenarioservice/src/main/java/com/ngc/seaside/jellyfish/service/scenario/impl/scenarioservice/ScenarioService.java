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
import com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario.PublishStepHandler;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario.ReceiveStepHandler;

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
      pubSubProcessor = new PubSubProcessor(publishStepHandler, receiveStepHandler);
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
         target = "(component.name=com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario.ReceiveStepHandler)")
   public void setReceiveStepHandler(IScenarioStepHandler ref) {
      this.receiveStepHandler = (ReceiveStepHandler) ref;
   }

   public void removeReceiveStepHandler(IScenarioStepHandler ref) {
      this.receiveStepHandler = null;
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         target = "(component.name=com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario.PublishStepHandler)")
   public void setPublishStepHandler(IScenarioStepHandler ref) {
      this.publishStepHandler = (PublishStepHandler) ref;
   }

   public void removePublishStepHandler(IScenarioStepHandler ref) {
      this.publishStepHandler = null;
   }
}
