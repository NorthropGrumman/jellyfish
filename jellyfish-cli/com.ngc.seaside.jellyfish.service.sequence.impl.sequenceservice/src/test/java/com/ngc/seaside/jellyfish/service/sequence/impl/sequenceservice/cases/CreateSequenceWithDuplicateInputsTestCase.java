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
package com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.cases;

import com.google.common.collect.Iterables;

import com.ngc.seaside.jellyfish.service.sequence.api.ISequence;
import com.ngc.seaside.jellyfish.service.sequence.api.ISequenceService;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.ItTestState;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test ensures that a sequence has duplicate outputs if duplicate inputs cause a flow to be executed multiple
 * times.
 */
public class CreateSequenceWithDuplicateInputsTestCase {

   private final ItTestState state;

   /**
    * Creates a new test.
    */
   public CreateSequenceWithDuplicateInputsTestCase(ItTestState state) {
      this.state = state;
   }

   /**
    * Runs the test.
    */
   public void execute() {
      IModel threatEval = state.getSystemDescriptor().findModel("com.ngc.seaside.threateval.ThreatEvaluation").get();
      IDataReferenceField systemTrackClassification =
            threatEval.getInputs().getByName("systemTrackClassification").get();
      IDataReferenceField prioritizedSystemTracks =
            threatEval.getOutputs().getByName("prioritizedSystemTracks").get();

      List<ISequence> sequences = state.getService().getSequences(
            state.getOptions(),
            threatEval,
            ISequenceService.GenerationStrategy.EXACT,
            Arrays.asList(systemTrackClassification, systemTrackClassification));
      assertEquals("did not generate the correct number of sequences!",
                   1,
                   sequences.size());

      ISequence sequence = sequences.get(0);
      assertEquals("sequence IDs should be sequential!",
                   1,
                   sequence.getId());
      assertEquals("model not correct!",
                   threatEval,
                   sequence.getModel());
      assertEquals("wrong number of inputs",
                   2,
                   sequence.getInputs().size());
      assertTrue("input for sequence not correct!",
                 sequence.getInputs().contains(systemTrackClassification));
      assertTrue("input for sequence not correct!",
                 sequence.getInputs().contains(systemTrackClassification));
      assertEquals("wrong number of outputs!",
                   2,
                   sequence.getOutputs().size());
      assertEquals("output for sequence not correct!",
                   prioritizedSystemTracks,
                   Iterables.get(sequence.getOutputs(), 0));
      assertEquals("output for sequence not correct!",
                   prioritizedSystemTracks,
                   Iterables.get(sequence.getOutputs(), 1));
   }
}
