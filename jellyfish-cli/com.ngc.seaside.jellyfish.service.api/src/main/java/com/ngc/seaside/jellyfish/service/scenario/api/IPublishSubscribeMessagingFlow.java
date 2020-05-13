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

import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;

import java.util.Collection;

/**
 * A type of flow that is used as part of the {@link MessagingParadigm#PUBLISH_SUBSCRIBE publish/subscribe} messaging
 * paradigm.  These types of flows come in three forms: a source which only publishes data, a sink which only subscribes
 * for data, and path which both subscribes and publishes.
 */
public interface IPublishSubscribeMessagingFlow extends IMessagingFlow {

   /**
    * Defines the different types of publish subscribe messaging flows.
    */
   enum FlowType {
      /**
       * A flow source is a flow that only publishes output.
       */
      SOURCE,
      /**
       * A flow sink is a flow that only subscribes to input.
       */
      SINK,
      /**
       * A flow path is a flow that subscribes to input and will publish output due to receipt of the input.
       */
      PATH
   }

   /**
    * Gets input fields received by this flow.  These are fields that are declared in the model that contains the
    * scenario that is associated with this flow.  Values are present only if this flow is a {@link FlowType#SINK} or
    * {@link FlowType#PATH}.
    *
    * @return the input fields received by this flow
    */
   Collection<IDataReferenceField> getInputs();

   /**
    * Gets the output fields published by this flow.  These are fields that are declared in the model that contains the
    * scenario that is associated with this flow.  Values are present only if this flow is a {@link FlowType#SOURCE} or
    * {@link FlowType#PATH}.
    *
    * @return the output fields of outputs published by this flow
    */
   Collection<IDataReferenceField> getOutputs();

   /**
    * Gets the type of publish/subscribe flow this flow is.
    *
    * @return the type of publish/subscribe flow this flow is
    */
   FlowType getFlowType();

   @Override
   default MessagingParadigm getMessagingParadigm() {
      return MessagingParadigm.PUBLISH_SUBSCRIBE;
   }
}
