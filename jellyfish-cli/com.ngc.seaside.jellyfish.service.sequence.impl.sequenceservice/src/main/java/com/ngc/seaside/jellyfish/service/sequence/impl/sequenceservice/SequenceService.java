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

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.jellyfish.service.sequence.api.ISequence;
import com.ngc.seaside.jellyfish.service.sequence.api.ISequenceService;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of {@code ISequenceService}.
 */
@Component(service = ISequenceService.class)
public class SequenceService implements ISequenceService {

   /**
    * The scenario service.
    */
   private IScenarioService scenarioService;

   /**
    * The log service.
    */
   private ILogService logService;

   @Override
   public List<ISequence> getSequences(IJellyFishCommandOptions options,
                                       IModel model,
                                       GenerationStrategy generationStrategy,
                                       Collection<IDataReferenceField> inputFields) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(model, "model may not be null!");
      Preconditions.checkNotNull(generationStrategy, "generationStrategy may not be null!");
      switch (generationStrategy) {
         case ALL_COMBINATIONS:
            return getSequencesByCombination(options, model, inputFields);
         case EXACT:
            return getSequencesByExactInput(options, model, inputFields);
         default:
            throw new IllegalArgumentException("unknown supported generation strategy: " + generationStrategy);
      }
   }

   @Activate
   public void activate() {
      logService.debug(getClass(), "Activated.");
   }

   @Deactivate
   public void deactivate() {
      logService.debug(getClass(), "Deactivated.");
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeScenarioService")
   public void setScenarioService(IScenarioService ref) {
      this.scenarioService = ref;
   }

   public void removeScenarioService(IScenarioService ref) {
      setScenarioService(null);
   }

   private List<ISequence> getSequencesByCombination(IJellyFishCommandOptions options,
                                                     IModel model,
                                                     Collection<IDataReferenceField> inputFields) {
      Preconditions.checkArgument(
            inputFields.size() <= 30,
            "cannot compute the sequences for more than 30 input fields since this results in more than"
            + " Integer.MAX_INT possibilities.  Given %d inputs.", inputFields.size());
      List<ISequence> sequences = new ArrayList<>();

      // We can reuse the generator.
      SequenceGenerator generator = new SequenceGenerator(scenarioService, logService)
            .setOptions(options)
            .setModel(model);
      // Models with lots of inputs can blow this up.  IE, a model with N inputs given us a total of 2^N possible
      // sequences.  This won't work for a model with more than 30 inputs (since that results in a set whose size is
      // greater than Integer.MAX_INT).
      // TODO TH: this won't work for duplicate inputs fields.  Throw an IAE if the inputFields collections has
      // duplications.
      for (Set<IDataReferenceField> inputs : Sets.powerSet(new LinkedHashSet<>(inputFields))) {
         // The power-set includes the empty set.  Ignore that set.
         if (!inputs.isEmpty()) {
            generator.generate(inputs, sequences.size() + 1).ifPresent(sequences::add);
         }
      }

      return sequences;
   }

   private List<ISequence> getSequencesByExactInput(IJellyFishCommandOptions options,
                                                    IModel model,
                                                    Collection<IDataReferenceField> inputFields) {
      return new SequenceGenerator(scenarioService, logService)
            .setOptions(options)
            .setModel(model)
            .generate(inputFields, 1)
            .map(Collections::singletonList)
            .orElse(Collections.emptyList());
   }
}
