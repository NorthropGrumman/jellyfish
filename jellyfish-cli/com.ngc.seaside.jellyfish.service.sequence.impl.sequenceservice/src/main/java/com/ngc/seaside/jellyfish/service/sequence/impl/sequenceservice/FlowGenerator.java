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
package com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice;

import com.google.common.collect.Sets;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.service.scenario.api.IMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IRequestResponseMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.jellyfish.service.scenario.correlation.api.ICorrelationDescription;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.model.SequenceFlow;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.model.SequenceFlowImplementation;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.model.SyntheticMessagingFlow;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * A flow generate is responsible for discovering all flows in a model.  It will also attempt to discover how each
 * flow is implemented using the {@link FlowImplementationGenerator}.
 */
public class FlowGenerator {

   private final Collection<SequenceFlow> flows = new ArrayList<>();

   /**
    * The scenario service used to discover scenarios for a model.
    */
   private final IScenarioService scenarioService;

   /**
    * Used for logging.
    */
   private final ILogService logService;

   /**
    * The context object used to generate the flow.
    */
   private FlowGeneratorContext flowContext;

   /**
    * Creates a new {@code FlowGenerator}.
    *
    * @param scenarioService the scenario service
    * @param logService      the log service
    */
   public FlowGenerator(IScenarioService scenarioService, ILogService logService) {
      this.scenarioService = scenarioService;
      this.logService = logService;
   }

   /**
    * Attempts to generate flows.
    *
    * @param flowContext the context object that configures the generator
    * @return the generated flows
    */
   public Collection<SequenceFlow> generateFlows(FlowGeneratorContext flowContext) {
      this.flowContext = flowContext;

      logService.trace(FlowGenerator.class,
                       "Generating flows for %s with inputs %s.",
                       flowContext.getModel().getFullyQualifiedName(),
                       flowContext.getInputs().stream().map(INamedChild::getName).collect(Collectors.joining(", ")));

      // Find any flows declared as scenarios.
      discoverDeclaredFlows();

      // We don't care about discovering undeclared flow right now.  This feature is disabled.
      // discoverUndeclaredFlows();

      logService.trace(FlowGenerator.class,
                       "Finished generating %d flows for %s.",
                       flows.size(),
                       flowContext.getModel().getFullyQualifiedName());

      return flows;
   }

   /**
    * Discovers any declared flows.  These are flows which are declared using scenarios.  Implementations will also
    * attempt to be generated.
    */
   private void discoverDeclaredFlows() {
      // Find any declared flows.
      exploreScenarios();

      // Try to find implementations for each declared flow.
      for (SequenceFlow flow : flows) {
         // Try to generate an implementation of the flow.
         flow.setImplementation(new FlowImplementationGenerator(scenarioService, logService).generateImplementation(
               new FlowImplementationGeneratorContext()
                     .setFlow(flow)
                     .setOptions(flowContext.getOptions())));
      }
   }

   /**
    * Discovered any undeclared flows.  Undeclared flows are flows which are not declared explicitly with scenario but
    * do actually exists.  These types of flow exists because a high level model may link inputs or outputs to its
    * sub-components (ie, parts).  These sub-components may themselves have declared scenarios.  So, in effect, the
    * higher level model also contains this behavior/scenarios it just isn't declared.
    */
   private void discoverUndeclaredFlows() {
      // This implementation will capture any undeclared flows as a single undeclared implementation.  This means
      // instead of generating multiple undeclared flows, we create a single implementation which itself contains any
      // number of flows.  This works because the sequence service logic will repeatably invoke the generators
      // for all combinations of inputs (if configured).  This ensures we will *eventually* discovered all undeclared
      // flows so we only need to look for one at a time here.
      SequenceFlowImplementation undeclaredFlow = new SyntheticFlowImplementationGenerator(scenarioService, logService)
            .generateSyntheticImplementation(new SyntheticFlowImplementationGeneratorContext()
                                                   .setOptions(flowContext.getOptions())
                                                   .setModel(flowContext.getModel())
                                                   .setDeclaredFlows(flows)
                                                   .setInputs(flowContext.getInputs()));

      if (undeclaredFlow != null) {
         // Determine how many times the undeclared flow can be activated.
         Collection<IDataReferenceField> inputs = new ArrayList<>(flowContext.getInputs());
         while (canFlowBeActivated(undeclaredFlow, inputs)) {
            inputs = flowActivated(undeclaredFlow, inputs, 1);
         }
      }
   }

   /**
    * Explores scenarios in the model, generating flows as necessary.
    */
   private void exploreScenarios() {
      // Check to see which scenarios (if any) reference any of the inputs.
      for (IScenario scenario : flowContext.getModel().getScenarios()) {
         // Check for pub/sub flows that reference the current inputs.
         scenarioService.getPubSubMessagingFlow(flowContext.getOptions(), scenario)
               .ifPresent(this::explorePubSubFlow);
         // Check for req/res flows that reference the current inputs.
         scenarioService.getRequestResponseMessagingFlow(flowContext.getOptions(), scenario)
               .ifPresent(this::exploreReqResFlow);
      }
   }

   /**
    * Explores the given pub/sub flow and determines if the flow can be activated.
    *
    * @param flow the pub/sub flow to explore
    */
   private void explorePubSubFlow(IPublishSubscribeMessagingFlow flow) {
      Collection<IDataReferenceField> inputs = new ArrayList<>(flowContext.getInputs());
      // Does this flow reference the any of the available inputs?
      while (canFlowBeActivated(flow, inputs)) {
         // If so, activate the flow and remove the inputs the flow consumed.
         inputs = flowActivated(flow, inputs);
         // Continue the loop until the flow can't be activated any more.  We do this in case a single scenario
         // is activated more than once because duplicate inputs are available.
      }
   }

   /**
    * Explores the given req/res flow and determines if the flow can be activated.
    *
    * @param flow the req/res flow to explore
    */
   private void exploreReqResFlow(IRequestResponseMessagingFlow flow) {
      Collection<IDataReferenceField> inputs = new ArrayList<>(flowContext.getInputs());
      // Does this flow reference the any of the available inputs?
      while (canFlowBeActivated(flow, inputs)) {
         // If so, activate the flow and remove the inputs the flow consumed.
         inputs = flowActivated(flow, inputs);
         // Continue the loop until the flow can't be activated any more.  We do this in case a single scenario
         // is activated more than once because duplicate inputs are available.
      }
   }

   /**
    * Returns true if the given pub/sub flow can be activated with any of the given inputs.
    *
    * @param flow   the flow
    * @param inputs the inputs
    * @return true if the flow can be activated
    */
   private boolean canFlowBeActivated(IPublishSubscribeMessagingFlow flow, Collection<IDataReferenceField> inputs) {
      // The flow can only be activated under two conditions:
      // 1) at least one input referenced in flow is in scope
      // 2) all the inputs required (as given by the flow's correlation description) are in scope
      boolean activated = !Sets.intersection(new HashSet<>(inputs),
                                             new HashSet<>(flow.getInputs())).isEmpty();
      if (activated && flow.getCorrelationDescription().isPresent()) {
         ICorrelationDescription correlation = flow.getCorrelationDescription().get();
         for (IDataReferenceField input : flow.getInputs()) {
            // Check to see if the input is mentioned in a correlation.  If so, then the input must be in the current
            // scope to activate the flow.
            activated &= correlation.getCompletenessExpressionForInput(input).isEmpty()
                         || inputs.contains(input);
         }
      }
      return activated;
   }

   /**
    * Returns true if the given pub/sub flow can be activated with any of the given inputs.
    *
    * @param flow   the flow
    * @param inputs the inputs
    * @return true if the flow can be activated
    */
   private boolean canFlowBeActivated(IRequestResponseMessagingFlow flow, Collection<IDataReferenceField> inputs) {
      // A req/res flow can be activated as long as we have the necessary input.
      return inputs.contains(flow.getInput());
   }

   /**
    * Returns true if the given implementation can be activated.  This method is used for synthetic flows.
    *
    * @param impl   the implementation
    * @param inputs the inputs
    * @return true if the flow can be activated
    */
   private boolean canFlowBeActivated(SequenceFlowImplementation impl, Collection<IDataReferenceField> inputs) {
      return impl.getIncomingLinks()
            .stream()
            .allMatch(l -> inputs.contains(l.getSource()));
   }

   /**
    * Invoked when a flow has been activated.  Create a sequence flow and adds it the list of current flow.
    *
    * @param flow   the flow that was activated
    * @param inputs the inputs
    * @return the resulting inputs after removing the inputs that the activated flow consumed
    */
   private Collection<IDataReferenceField> flowActivated(IMessagingFlow flow, Collection<IDataReferenceField> inputs) {
      logService.trace(FlowGenerator.class,
                       "Determined that scenario %s.%s can be activated.",
                       flow.getScenario().getParent().getName(),
                       flow.getScenario().getName());

      // Create a new sequence flow that wraps the given messaging flow.  The sequence flow just had additional
      // information with it.
      SequenceFlow sequenceFlow = new SequenceFlow(flow)
            .setSequenceNumber(flowContext.getSequenceNumber())
            .addInputs(getConsumedInputs(flow, inputs))
            .addOutputs(Sequencing.getOutputs(flow));
      flows.add(sequenceFlow);

      // Remove the input so we can determine if the scenario can be reactivated even with the current input removed.
      Collection<IDataReferenceField> newInputs = new ArrayList<>(inputs);
      // Don't use removeAll since that will remove duplicate input fields (we only want to remove one).
      sequenceFlow.getInputs().forEach(newInputs::remove);
      return newInputs;
   }

   /**
    * Invoked when an undeclared or synthetic flow has been activated.  Create a sequence flow and adds it the list of
    * current flow.
    *
    * @param undeclaredFlow      the  synthetic flow that was activated
    * @param inputs              the inputs
    * @param undeclaredFlowIndex the index number of the undeclared flow
    * @return the resulting inputs after removing the inputs that the activated flow consumed
    */
   private Collection<IDataReferenceField> flowActivated(SequenceFlowImplementation undeclaredFlow,
                                                         Collection<IDataReferenceField> inputs,
                                                         int undeclaredFlowIndex) {

      // Create the full flow and add it to the list.
      SyntheticMessagingFlow messageFlow = new SyntheticMessagingFlow()
            .setScenarioName("<undeclared-scenario-#" + undeclaredFlowIndex + ">")
            .setModel(flowContext.getModel())
            .setInputs(getConsumedInputs(undeclaredFlow))
            .setOutputs(Sequencing.getOutputs(undeclaredFlow));
      SequenceFlow flow = new SequenceFlow(messageFlow)
            .setImplementation(undeclaredFlow)
            .setSequenceNumber(flowContext.getSequenceNumber())
            .addInputs(messageFlow.getInputs())
            .addOutputs(messageFlow.getOutputs());
      undeclaredFlow.setParentFlow(flow);
      flows.add(flow);

      // Remove the input so we can determine if the scenario can be reactivated even with the current input removed.
      Collection<IDataReferenceField> newInputs = new ArrayList<>(inputs);
      // Don't use removeAll since that will remove duplicate input fields (we only want to remove one).
      flow.getInputs().forEach(newInputs::remove);
      return newInputs;
   }

   private static Collection<IDataReferenceField> getConsumedInputs(IMessagingFlow flow,
                                                                    Collection<IDataReferenceField> inputs) {
      return Sets.intersection(new HashSet<>(inputs), new HashSet<>(Sequencing.getInputs(flow)));
   }


   private static Collection<IDataReferenceField> getConsumedInputs(SequenceFlowImplementation undeclaredFlow) {
      return undeclaredFlow.getIncomingLinks()
            .stream()
            .map(IModelLink::getSource)
            .distinct()
            .collect(Collectors.toList());
   }
}
