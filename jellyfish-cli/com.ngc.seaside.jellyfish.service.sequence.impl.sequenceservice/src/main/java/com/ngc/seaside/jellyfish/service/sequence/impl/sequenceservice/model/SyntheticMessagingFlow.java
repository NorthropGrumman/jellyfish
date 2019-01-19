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
