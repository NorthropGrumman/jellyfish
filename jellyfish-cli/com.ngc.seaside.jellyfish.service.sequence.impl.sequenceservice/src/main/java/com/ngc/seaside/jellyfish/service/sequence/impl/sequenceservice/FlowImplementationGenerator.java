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
package com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.jellyfish.service.sequence.api.ISequenceFlow;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.model.SequenceFlow;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.model.SequenceFlowImplementation;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

/**
 * A generator that finds flow implementations.
 */
public class FlowImplementationGenerator extends AbstractFlowImplementationGenerator {

   /**
    * The context object that contains the configuration of the generator.
    */
   private FlowImplementationGeneratorContext context;

   /**
    * Creates a new {@code FlowImplementationGenerator}.
    *
    * @param scenarioService the scenario service
    * @param logService      the log service
    */
   public FlowImplementationGenerator(IScenarioService scenarioService, ILogService logService) {
      super(scenarioService, logService);
   }

   /**
    * Attempts to create an implementation.  May return {@code null} if no implementation was found.
    *
    * @param context the context object that configures the generator
    * @return the implementation or {@code null} if no implementation was found
    */
   public SequenceFlowImplementation generateImplementation(FlowImplementationGeneratorContext context) {
      this.context = context;
      this.options = context.getOptions();
      IScenario scenario = context.getFlow().getMessagingFlow().getScenario();
      IModel model = scenario.getParent();

      logService.trace(FlowImplementationGenerator.class,
                       "Attempting to find implementation of %s.%s.",
                       model.getFullyQualifiedName(),
                       scenario.getName());

      SequenceFlowImplementation impl = findImpl();
      if (impl == null) {
         logService.trace(FlowImplementationGenerator.class,
                          "Did not find implementation of %s.%s.",
                          model.getFullyQualifiedName(),
                          scenario.getName());
      } else {
         logService.trace(FlowImplementationGenerator.class,
                          "Found implementation of %s.%s.",
                          model.getFullyQualifiedName(),
                          scenario.getName());
      }

      return impl;
   }

   /**
    * Finds the implementation of the flow in the context or {@code null}.
    *
    * @return the implementatino
    */
   private SequenceFlowImplementation findImpl() {
      IScenario scenario = context.getFlow().getMessagingFlow().getScenario();
      IModel model = scenario.getParent();

      SequenceFlowImplementation impl = new SequenceFlowImplementation().setParentFlow(context.getFlow());
      populateImpl(impl, model, context.getFlow().getInputs());

      // Check to make sure the impl is valid.  This means the actual inputs and outputs of the impl must match
      // the flow.  Otherwise, we didn't actually find an impl.
      boolean implValid = isImplValid(impl);

      // If the implementation is valid, try to find the implementation of any nested flows.
      if (implValid) {
         logService.trace(FlowImplementationGenerator.class,
                          "Recursively searching for implementation of %d nested flows of %s.%s.",
                          impl.getFlows().size(),
                          model.getFullyQualifiedName(),
                          scenario.getName());
         for (ISequenceFlow nestedFlow : impl.getFlows()) {
            // Safe because we created the flow.
            SequenceFlow casted = (SequenceFlow) nestedFlow;
            casted.setImplementation(new FlowImplementationGenerator(scenarioService, logService)
                                           .generateImplementation(new FlowImplementationGeneratorContext()
                                                                         .setFlow(casted)
                                                                         .setOptions(context.getOptions())));
         }
      }

      return implValid ? impl : null;
   }

   /**
    * Returns true if the discovered implementation is valid given the declared flow.
    *
    * @param impl the discovered impl
    * @return true if the implementation is valid
    */
   private boolean isImplValid(SequenceFlowImplementation impl) {
      // Make sure the outputs above and the outputs of the actual flow match.  If they do, this impl is valid
      // for the given flow.
      return Sequencing.equivalent(Sequencing.getOutputs(impl), context.getFlow().getOutputs());
   }
}
