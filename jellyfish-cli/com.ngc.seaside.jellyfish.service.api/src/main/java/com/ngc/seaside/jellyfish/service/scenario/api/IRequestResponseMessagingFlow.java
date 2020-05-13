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
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.util.Optional;

/**
 * A type of flow that is used as part of the {@link MessagingParadigm#REQUEST_RESPONSE request/response} messaging
 * paradigm.  A request/response flow comes in two forms: the client side flow that initiations a request and receives
 * the response and the server side flow that receives a request and responds.
 */
public interface IRequestResponseMessagingFlow extends IMessagingFlow {

   /**
    * Defines the different kinds of request/response messaging flows.
    */
   enum FlowType {
      /**
       * A client flow indicates that this flow describes a client sending a request to a server and waiting for the
       * response from the server.  This flow is from the perspective of the client.  The output of these types of
       * flows is the request to the server and the input is the response from the server.
       */
      CLIENT,
      /**
       * A server flow indicates that this flow describes a server receiving a request and responding to it.  This flow
       * is from the perspective of the server.  The input of these types of flows is the request to the server from the
       * client and the output of these types of flows is the response from the server to the client.
       */
      SERVER
   }

   /**
    * Gets the input fields of this flow.  These are fields that are declared in the model that contains the
    * scenario that is associated with this flow.
    *
    * @return the input fields of this flow
    */
   IDataReferenceField getInput();

   /**
    * Gets the output fields of this flow.  These are fields that are declared in the model that contains the
    * scenario that is associated with this flow.
    *
    * @return the output fields of this flow
    */
   IDataReferenceField getOutput();

   /**
    * Gets the type of request/response flow this flow is.
    *
    * @return the type of request/response flow this flow is
    */
   FlowType getFlowType();

   /**
    * Gets the part declaration that points to the model of the server side component that is being invoked by a client.
    * The {@link IModelReferenceField} is a field that is declared in the model for the {@link #getScenario() scenario}
    * that is associated with this flow. This value is only set if this is a {@link FlowType#CLIENT client-side} flow.
    *
    * @return an optional that contains the model of the invoked server side component if this flow is a client side
    * flow; otherwise the optional is empty.
    */
   Optional<IModelReferenceField> getInvokedServerSideComponent();

   /**
    * Gets the scenario that invoked on the server component.  This is not a scenario that is declared in the {@link
    * #getScenario() scenario that is assoicated with this flow}, but the scenario that is associated with the
    * server-side flow.  This value is only set if this is a {@link FlowType#CLIENT client-side} flow.
    *
    * @return an optional that contains the invoked scenario on server side if this flow is a client side flow;
    * otherwise the optional is empty.
    */
   Optional<IScenario> getInvokedServerSideScenario();

   @Override
   default MessagingParadigm getMessagingParadigm() {
      return MessagingParadigm.REQUEST_RESPONSE;
   }
}
