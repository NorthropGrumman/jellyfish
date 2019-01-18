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
package com.ngc.seaside.jellyfish.service.scenario.api;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.util.Collection;
import java.util.Optional;

/**
 * The {@code IScenarioService} is responsible for interpreting {@link IScenario}s and aiding with handling scenarios.
 */
public interface IScenarioService {

   /**
    * Gets the messaging paradigms that are applicable to the given scenario. A single scenario may declare multiple
    * paradigms.
    *
    * @param options  the options the current command is being executed with
    * @param scenario the scenario to get the messaging paradigms for
    * @return the messaging paradigms that are applicable to the given scenario
    */
   Collection<MessagingParadigm> getMessagingParadigms(IJellyFishCommandOptions options,
                                                       IScenario scenario);

   /**
    * Gets the publish/subscribe messaging flow that the given scenario declares or {@link Optional#empty()} if the
    * scenario does not deal with publishing or subscribing. The flow is either a {@link
    * IPublishSubscribeMessagingFlow.FlowType#PATH path}, a {@link IPublishSubscribeMessagingFlow.FlowType#PATH source},
    * or {@link IPublishSubscribeMessagingFlow.FlowType#PATH sink}.
    *
    * @param options  the options the current command is being executed with
    * @param scenario the scenario to get the messaging flows for
    * @return the publish/subscribe messaging flow that the given scenario declares
    */
   Optional<IPublishSubscribeMessagingFlow> getPubSubMessagingFlow(IJellyFishCommandOptions options,
                                                                   IScenario scenario);

   /**
    * Gets the request/response messaging flow that the given scenario declares or {@link Optional#empty()} if the
    * scenario does not detail with request or response. The flow is either a {@link
    * IRequestResponseMessagingFlow.FlowType#CLIENT client flow} or a
    * {@link IRequestResponseMessagingFlow.FlowType#SERVER
    * server flow}.
    *
    * @param options  the options the current command is being executed with
    * @param scenario the scenario to get the messaging flow for
    * @return the request/response messaging flow that the given scenario declares
    */
   Optional<IRequestResponseMessagingFlow> getRequestResponseMessagingFlow(IJellyFishCommandOptions options,
                                                                           IScenario scenario);

   /**
    * Gets a collection of timing constraints that have been applied to the given scenario.
    *
    * @param options  the options the current command is being executed with
    * @param scenario the scenario to get the timing constraints for
    * @return a collection of timing constraints that have been applied to the given scenario
    */
   Collection<ITimingConstraint> getTimingConstraints(IJellyFishCommandOptions options, IScenario scenario);

}
