package com.ngc.seaside.jellyfish.service.scenario.api;

import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

/**
 * A messaging flow represents a logical flow of inputs and outputs through a component.  Flows are derived from {@link
 * IScenario scenario}s.  A scenario may have one or more flows that can be derived from it depending on the scenario
 * steps and verbs used in its construction.  Flows are associated with messaging paradigms which defines how the inputs
 * and outputs are handled.
 *
 * <p/>
 *
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
}
