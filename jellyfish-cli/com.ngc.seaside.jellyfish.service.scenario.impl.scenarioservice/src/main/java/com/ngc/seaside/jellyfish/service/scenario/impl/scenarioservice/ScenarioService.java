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
import com.ngc.seaside.jellyfish.service.scenario.impl.scenarioservice.processor.RequestResponseProcessor;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.scenario.api.IScenarioStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.CorrelateStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.PublishStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.ReceiveRequestStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.ReceiveStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.RespondStepHandler;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

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
   private RequestResponseProcessor reqResProcesssor;

   private ReceiveStepHandler receiveStepHandler;
   private PublishStepHandler publishStepHandler;
   private CorrelateStepHandler correlateStepHandler;
   private ReceiveRequestStepHandler receiveRequestStepHandler;
   private RespondStepHandler respondStepHandler;
   private ILogService logService;

   @Override
   public Collection<MessagingParadigm> getMessagingParadigms(IJellyFishCommandOptions options, IScenario scenario) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(scenario, "scenario may not be null!");

      Set<MessagingParadigm> paradigms = EnumSet.noneOf(MessagingParadigm.class);
      if (pubSubProcessor.isPublishSubscribe(scenario)) {
         paradigms.add(MessagingParadigm.PUBLISH_SUBSCRIBE);
      }
      if (reqResProcesssor.isRequestResponse(scenario)) {
         paradigms.add(MessagingParadigm.REQUEST_RESPONSE);
      }

      return Collections.unmodifiableCollection(paradigms);
   }

   @Override
   public Optional<IPublishSubscribeMessagingFlow> getPubSubMessagingFlow(IJellyFishCommandOptions options,
                                                                          IScenario scenario) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(scenario, "scenario may not be null!");
      return pubSubProcessor.getFlow(scenario);
   }

   @Override
   public Optional<IRequestResponseMessagingFlow> getRequestResponseMessagingFlow(IJellyFishCommandOptions options,
                                                                                   IScenario scenario) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(scenario, "scenario may not be null!");
      return reqResProcesssor.getFlow(scenario);
   }

   @Override
   public Collection<ITimingConstraint> getTimingConstraints(IJellyFishCommandOptions options, IScenario scenario) {
      // Note this logic is the same regardless of messaging paradigm.
      throw new UnsupportedOperationException("not implemented");
   }

   @Activate
   public void activate() {
      pubSubProcessor = new PubSubProcessor(publishStepHandler,
                                            receiveStepHandler,
                                            correlateStepHandler);
      reqResProcesssor = new RequestResponseProcessor(receiveRequestStepHandler,
                                                      respondStepHandler);
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
         target = "(component.name=com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.ReceiveStepHandler)")
   public void setReceiveStepHandler(IScenarioStepHandler ref) {
      receiveStepHandler = (ReceiveStepHandler) ref;
   }

   public void removeReceiveStepHandler(IScenarioStepHandler ref) {
      setReceiveStepHandler(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         target = "(component.name=com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.PublishStepHandler)")
   public void setPublishStepHandler(IScenarioStepHandler ref) {
      publishStepHandler = (PublishStepHandler) ref;
   }

   public void removePublishStepHandler(IScenarioStepHandler ref) {
      setPublishStepHandler(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         target = "(component.name=com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.CorrelateStepHandler)")
   public void setCorrelationStepHandler(IScenarioStepHandler ref) {
      correlateStepHandler = (CorrelateStepHandler) ref;
   }

   public void removeCorrelationStepHandler(IScenarioStepHandler ref) {
      setCorrelationStepHandler(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         target = "(component.name=com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.ReceiveRequestStepHandler)")
   public void setReceiveRequestStepHandler(IScenarioStepHandler ref) {
      receiveRequestStepHandler = (ReceiveRequestStepHandler) ref;
   }

   public void removeReceiveRequestStepHandler(IScenarioStepHandler ref) {
      setReceiveRequestStepHandler(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         target = "(component.name=com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.RespondStepHandler)")
   public void setRespondStepHandler(IScenarioStepHandler ref) {
      respondStepHandler = (RespondStepHandler) ref;
   }

   public void removeRespondRequestStepHandler(IScenarioStepHandler ref) {
      setRespondStepHandler(null);
   }
}
