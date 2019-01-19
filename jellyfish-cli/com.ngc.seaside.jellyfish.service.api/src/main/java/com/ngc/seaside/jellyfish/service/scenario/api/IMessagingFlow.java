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
package com.ngc.seaside.jellyfish.service.scenario.api;

import com.ngc.seaside.jellyfish.service.scenario.correlation.api.ICorrelationDescription;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.util.Optional;

/**
 * A messaging flow represents a logical flow of inputs and outputs through a component.  Flows are derived from {@link
 * IScenario scenario}s.  A scenario may have one or more flows that can be derived from it depending on the scenario
 * steps and verbs used in its construction.  Flows are associated with messaging paradigms which defines how the inputs
 * and outputs are handled.
 * <p/>
 * Flows are treated in a black box manner.  Flows declare the inputs a component have receive and the outputs it may
 * produce but flows reveal no information about how a component is implemented.
 */
public interface IMessagingFlow {

   /**
    * Gets the messaging paradigm that this flow is a member of.  A flow only supports a single type of messaging
    * paradigm.
    */
   MessagingParadigm getMessagingParadigm();

   /**
    * Gets the scenario this messaging flow was generated from.
    *
    * @return the scenario this messaging flow was generated from
    */
   IScenario getScenario();

   /**
    * Gets the correlation description for this scenario provided this scenario involves correlation.  If this scenario
    * does not involve correlation, the returned optional is empty.
    *
    * @return the correlation description for this scenario as a potentially empty optional
    */
   Optional<ICorrelationDescription> getCorrelationDescription();
}
