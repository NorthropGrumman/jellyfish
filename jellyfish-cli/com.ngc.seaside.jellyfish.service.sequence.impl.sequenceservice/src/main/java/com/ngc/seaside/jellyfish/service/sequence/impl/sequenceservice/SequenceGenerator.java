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

import com.google.common.base.Preconditions;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.jellyfish.service.sequence.api.ISequence;
import com.ngc.seaside.jellyfish.service.sequence.api.ISequenceFlow;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.model.Sequence;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.model.SequenceFlow;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Generates a sequence.
 */
public class SequenceGenerator {

   /**
    * The scenario service used to discover scenarios for a model.
    */
   private final IScenarioService scenarioService;

   /**
    * Used for logging.
    */
   private final ILogService logService;

   /**
    * The options Jellyfish was invoked with.
    */
   private IJellyFishCommandOptions options;

   /**
    * The model to generate sequences for.
    */
   private IModel model;

   /**
    * The context object that contains the configuration of the generator.
    */
   private SequenceGeneratorContext generatorContext;

   /**
    * Creates a new generator.
    *
    * @param scenarioService the scenario service
    * @param logService      the log service
    */
   public SequenceGenerator(IScenarioService scenarioService, ILogService logService) {
      this.scenarioService = scenarioService;
      this.logService = logService;
   }

   /**
    * Sets the options to use when generating sequences.
    *
    * @param options the options to use when generating sequences
    * @return this generator
    */
   public SequenceGenerator setOptions(IJellyFishCommandOptions options) {
      this.options = options;
      return this;
   }

   /**
    * Sets the model to use when generating sequences.
    *
    * @param model the model to use when generating sequences
    * @return this generator
    */
   public SequenceGenerator setModel(IModel model) {
      this.model = model;
      return this;
   }

   /**
    * Attempts to generate a sequence with the provided inputs.  If the sequence is generated, it will have the given
    * ID.  If no sequence can be generated with the inputs, the returned optional is empty.  A sequence is only
    * considered valid if it uses all of the provided input fields.  The model and options must be set before invoking
    * this method.
    *
    * @param inputs the inputs to use to generate the sequence.
    * @param id     the ID of the generate sequence
    * @return an optional that contains the sequence or an empty optional of the no valid sequence could be generated
    */
   public Optional<ISequence> generate(Collection<IDataReferenceField> inputs, int id) {
      Preconditions.checkNotNull(inputs, "inputs may not be null!");
      Preconditions.checkArgument(!inputs.isEmpty(), "inputs should not be empty!");
      Preconditions.checkState(options != null, "options not set!");
      Preconditions.checkState(model != null, "model not set!");
      Optional<ISequence> result = Optional.empty();

      if (logService.isTraceEnabled(SequenceGenerator.class)) {
         logService.trace(SequenceGenerator.class,
                          "Generating sequence for %s with the inputs: %s.",
                          model.getFullyQualifiedName(),
                          inputs.stream().map(INamedChild::getName).collect(Collectors.joining(",")));
      }

      // Create the sequence and set the initial data.
      Sequence sequence = new Sequence(id);
      sequence.setModel(model);
      sequence.addInputs(inputs);

      // Create a context and see if we can generate the sequence given the inputs.
      generatorContext = new SequenceGeneratorContext()
            .setSequence(sequence)
            .setInputs(inputs);
      doGenerate();

      // Determine if the resulting sequence is valid.  If not, discard the sequence.
      if (!shouldSequenceBeDiscarded()) {
         // Do final processing on the sequence.  Figure out final outputs of the entire sequence.
         setSequenceOutputs();
         result = Optional.of(sequence);
      } else {
         logService.trace(SequenceGenerator.class, "Sequence is being discarded because not all inputs were used.");
      }

      logService.trace(SequenceGenerator.class, "Sequence generating for %s complete.", model.getFullyQualifiedName());

      return result;
   }

   private void doGenerate() {
      // First generate the flows that have been declared as scenarios.
      generateScenarioFlows();
   }

   private void generateScenarioFlows() {
      Collection<SequenceFlow> flows = new FlowGenerator(scenarioService, logService)
            .generateFlows(new FlowGeneratorContext()
                                 .setModel(model)
                                 .setSequenceNumber(0)
                                 .setOptions(options)
                                 .setInputs(generatorContext.getInputs()));
      generatorContext.getSequence().addFlows(flows);

      for (SequenceFlow flow : flows) {
         // Mark the inputs to the flow as consumed.  We do this so we can determine if the sequence eventfully
         // used all the inputs that were originally passed to it.
         Collection<IDataReferenceField> flowInputs = Sequencing.getInputs(flow.getMessagingFlow());
         generatorContext.getConsumedInputs().addAll(flowInputs);
      }
   }

   /**
    * Returns true if the sequence should be discarded because the sequence has incomplete frames or flows.
    *
    * @return true if the sequence should be discarded
    */
   private boolean shouldSequenceBeDiscarded() {
      // Only keep this sequence if it has at least one frame.
      boolean discard = generatorContext.getSequence().getFlows().isEmpty();
      // Where all of the inputs to this sequence consumed?  If not, then discard this sequence.  This sequence
      // will get recreated later when we hit the combination of inputs that are actually required by the sequence.
      if (!discard) {
         discard = generatorContext.getSequence().getInputs()
               .stream()
               .anyMatch(i -> !generatorContext.getConsumedInputs().contains(i));
      }
      return discard;
   }

   /**
    * Sets the outputs of the entire sequence.  These outputs consists of the outputs of all the flows of the last frame
    * provided there is a link that connects the output of each flow to an output of the model that contains the
    * sequence.
    */
   private void setSequenceOutputs() {
      // The outputs of the sequence are the outputs of the flows.
      IModel model = generatorContext.getSequence().getModel();
      for (ISequenceFlow flow : generatorContext.getSequence().getFlows()) {
         Sequencing.getOutputs(flow.getMessagingFlow())
               .stream()
               .filter(output -> model.getOutputs().contains(output))
               .forEach(output -> generatorContext.getSequence().addOutput(output));
      }
   }
}
