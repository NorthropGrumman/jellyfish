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
