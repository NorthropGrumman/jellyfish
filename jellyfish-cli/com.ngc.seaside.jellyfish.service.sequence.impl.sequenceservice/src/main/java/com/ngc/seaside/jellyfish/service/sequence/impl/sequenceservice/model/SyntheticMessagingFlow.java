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
package com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.model;

import com.ngc.seaside.jellyfish.service.scenario.api.IMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.MessagingParadigm;
import com.ngc.seaside.jellyfish.service.scenario.correlation.api.ICorrelationDescription;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * A synthetic messaging flow is a type of {@link IMessagingFlow} which has no explicit scenario defined in a model.
 * This type of flow is used when it is necessary to crate a synthetic sequence flow.  These types of synthetic flows
 * are possible because a model's sub-components have declared scenarios and because the high level model links its
 * own inputs and outputs to these sub-components.  As a result, the high level model actually contains flows which
 * can be derived from it sub-components flows.  This class is used when we need to construct such a synthetic flow.
 */
public class SyntheticMessagingFlow implements IMessagingFlow {

   // Note this functionally is currently disabled.  See comment in FlowGenerator.java.

   /**
    * The inputs to this flow.
    */
   private final Collection<IDataReferenceField> inputs = new ArrayList<>();

   /**
    * The outputs of this flow.
    */
   private final Collection<IDataReferenceField> outputs = new ArrayList<>();

   /**
    * The synthetic scenario associated with this flow.  This is basically a generated scenario.
    */
   private final SyntheticScenario scenario = new SyntheticScenario();

   /**
    * The model associated with this flow.
    */
   private IModel model;

   /**
    * The generated name of this scenario.
    */
   private String scenarioName;

   @Override
   public MessagingParadigm getMessagingParadigm() {
      // TODO TH: make this dynamic by discovering the types of nested flows and using that.
      return MessagingParadigm.PUBLISH_SUBSCRIBE;
   }

   @Override
   public IScenario getScenario() {
      return scenario;
   }

   @Override
   public Optional<ICorrelationDescription> getCorrelationDescription() {
      // Undeclared/synthetic flows cannot be correlated.  This must be done explicitly.
      return Optional.empty();
   }

   /**
    * Gets the inputs to this flow.
    *
    * @return the outputs
    */
   public Collection<IDataReferenceField> getInputs() {
      return inputs;
   }

   /**
    * Clears any existing inputs to this flow and sets the inputs to the given value.
    *
    * @param inputs the new inputs
    * @return this flow
    */
   public SyntheticMessagingFlow setInputs(Collection<IDataReferenceField> inputs) {
      this.inputs.clear();
      this.inputs.addAll(inputs);
      return this;
   }

   /**
    * Gets the outputs to this flow.
    *
    * @return the outputs
    */
   public Collection<IDataReferenceField> getOutputs() {
      return outputs;
   }

   /**
    * Clears any existing outputs to this flow and sets the outputs to the given value.
    *
    * @param outputs the new outputs
    * @return this flow
    */
   public SyntheticMessagingFlow setOutputs(Collection<IDataReferenceField> outputs) {
      this.outputs.clear();
      this.outputs.addAll(outputs);
      return this;
   }

   /**
    * Sets the name of the scenario for this flow.
    *
    * @param scenarioName the name of the scenario for this flow
    * @return this flow
    */
   public SyntheticMessagingFlow setScenarioName(String scenarioName) {
      this.scenarioName = scenarioName;
      return this;
   }

   /**
    * Sets the model for this flow.
    *
    * @param model the model for this flow
    * @return this flow
    */
   public SyntheticMessagingFlow setModel(IModel model) {
      this.model = model;
      return this;
   }

   /**
    * Implementation of {@code IScenario} that can be used for "generated" scenarios.
    */
   private class SyntheticScenario implements IScenario {

      @Override
      public IMetadata getMetadata() {
         return IMetadata.EMPTY_METADATA;
      }

      @Override
      public IScenario setMetadata(IMetadata metadata) {
         throw new UnsupportedOperationException("not implemented");
      }

      @Override
      public Collection<IScenarioStep> getGivens() {
         return Collections.emptyList();
      }

      @Override
      public Collection<IScenarioStep> getWhens() {
         return Collections.emptyList();
      }

      @Override
      public Collection<IScenarioStep> getThens() {
         return Collections.emptyList();
      }

      @Override
      public String getName() {
         return scenarioName;
      }

      @Override
      public IModel getParent() {
         return model;
      }
   }
}
