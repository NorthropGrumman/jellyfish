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
package com.ngc.seaside.jellyfish.service.sequence.api;

import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;

import java.util.Collection;
import java.util.Map;

/**
 * Describes how a particular {@link ISequenceFlow} is implemented.  Flows implementations may themselves contain
 * nested {@link #getFlows() flows}.  Each of these nested flows may also contain their own implementation.  Thus,
 * flow implementations are a direct reflection of how models are organized and decomposed.
 */
public interface ISequenceFlowImplementation {

   /**
    * Gets the flow that this implementation is for.  Calling {@link ISequenceFlow#getImplementation() getImplemntation}
    * this flow will return this implementation.
    *
    * @return the flow that this implementation is for
    */
   ISequenceFlow getParentFlow();

   /**
    * Gets the nested flows that describe how this flow is implemented.  Like a {@link ISequence}, this implementation
    * also acts as a container for other flows.  The returned flows are ordered by {@link
    * ISequenceFlow#getSequenceNumber sequence number}.
    *
    * @return the flows that describe how this flow is implemented.
    */
   Collection<ISequenceFlow> getFlows();

   /**
    * Gets the incoming links to this flow.  These are links whose sources are data fields given in the {@link
    * ISequenceFlow#getInputs inputs of the parent flow}.  These links are declared in the model that contains the
    * parent flow.
    *
    * @return the incoming links to this flow
    */
   Collection<IModelLink<IDataReferenceField>> getIncomingLinks();

   /**
    * Gets a map of incoming links for the given nested flow that is {@link #getFlows contained} in this
    * implementation.  The map is keyed by inputs of the parent flow.  Each value is the link traversed to get the
    * input to the given nested flow.
    *
    * @param flow the flow to get the incoming links for (this flow should be included in {@link #getFlows})
    * @return a map of incoming links for the given nested flow
    */
   Map<IDataReferenceField, IModelLink<IDataReferenceField>> getIncomingLinks(ISequenceFlow flow);

   /**
    * Gets the incoming link that was traversed to get the given input field to the given nested flow of this
    * implementation.  This operation is useful for determining which links where traversed to get inputs to
    * the parts or requirements of the model that contains the parent flow.
    *
    * @param flow  the nested flow (this flow should be included in {@link #getFlows()}
    * @param input the input (this input should be included in the {@link ISequenceFlow#getInputs parent flow's
    *              inputs}
    * @return the link was traversed to get the given input to the nested flow (this link is declared in the model that
    * contains the input field and the parent flow)
    * @see #getIncomingLink(ISequenceFlow, IDataReferenceField)
    */
   IModelLink<IDataReferenceField> getIncomingLink(ISequenceFlow flow, IDataReferenceField input);

   /**
    * Gets the outgoing links to this flow.  These are links whose targets are data fields given in the {@link
    * ISequenceFlow#getOutputs outputs of the parent flow}.  These links are declared in the model that contains the
    * parent flow.
    *
    * @return the outgoing links to this flow
    */
   Collection<IModelLink<IDataReferenceField>> getOutgoingLinks();

   /**
    * Gets a map of outgoing links for the given nested flow that is {@link #getFlows contained} in this
    * implementation.  The map is keyed by outputs of the parent flow.  Each value is the link traversed to get the
    * output out of the given nested flow.
    *
    * @param flow the flow to get the outgoing links for (this flow should be included in {@link #getFlows})
    * @return a map of outgoing links for the given nested flow
    * @see #getOutgoingLink(ISequenceFlow, IDataReferenceField)
    */
   Map<IDataReferenceField, IModelLink<IDataReferenceField>> getOutgoingLinks(ISequenceFlow flow);

   /**
    * Gets the outgoing link that was traversed to get the given output field out of the given nested flow of this
    * implementation.  This operation is useful for determining which links where traversed to get outputs from parts
    * or requirements out of this flow.
    *
    * @param flow   the nested flow (this flow should be included in {@link #getFlows})
    * @param output the output (this output should be included in the {@link ISequenceFlow#getOutputs parent flow's
    *               outputs}
    * @return the link was traversed to get the given output out of the nested flow (this link is declared in the model
    * that contains the input field and the parent flow)
    */
   IModelLink<IDataReferenceField> getOutgoingLink(ISequenceFlow flow, IDataReferenceField output);

   /**
    * Returns the model field which is implementing the given nested flow of this implementation.  This will be a part
    * field or a requirement of the model which contains the parent flow.  This operation is useful for determine which
    * sub-components of a model are actually participating in the implementation of a flow.  Note the {@link
    * IModelReferenceField#getType type} of the returned field
    * will be equal to the model that is associated with the given flow.  In other words, the following is true:
    * <pre>
    *    {@code field.getType() == flow.getMessageFlow().getScenario().getParent()}
    * </pre>
    *
    * @param flow the nested flow to get the field for
    * @return the field which describes the sub-component that is performing the given nested flow
    */
   IModelReferenceField getComponentImplementingFlow(ISequenceFlow flow);
}
