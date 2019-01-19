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
package com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.cases;

import com.google.common.collect.Iterables;

import com.ngc.seaside.jellyfish.service.scenario.api.MessagingParadigm;
import com.ngc.seaside.jellyfish.service.sequence.api.ISequence;
import com.ngc.seaside.jellyfish.service.sequence.api.ISequenceFlow;
import com.ngc.seaside.jellyfish.service.sequence.api.ISequenceService;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.ItTestState;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * This test verifies the sequence of a simple pub/sub component with a single scenario yields two flows because it
 * is activated twice due to the duplicate inputs.
 */
public class CreateSequenceForSinglePubSubComponentWithDuplicateInputsTestCase {

   private final ItTestState state;

   /**
    * Creates a new test.
    */
   public CreateSequenceForSinglePubSubComponentWithDuplicateInputsTestCase(ItTestState state) {
      this.state = state;
   }

   /**
    * Runs the test.
    */
   public void execute() {
      IModel model = state.getSystemDescriptor()
            .findModel("com.ngc.seaside.threateval.ClassificationTrackPriorityService")
            .get();
      IDataReferenceField systemTrackClassification = model.getInputs()
            .getByName("systemTrackClassification")
            .get();
      IDataReferenceField trackPriority = model.getOutputs()
            .getByName("trackPriority")
            .get();

      List<ISequence> sequences = state.getService().getSequences(
            state.getOptions(),
            model,
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
                   model,
                   sequence.getModel());
      assertEquals("wrong input for sequence!",
                   systemTrackClassification,
                   Iterables.get(sequence.getInputs(), 0));
      assertEquals("wrong number of inputs",
                   2,
                   sequence.getInputs().size());
      assertEquals("wrong output for sequence!",
                   trackPriority,
                   Iterables.get(sequence.getOutputs(), 0));
      assertEquals("wrong number of outputs!",
                   2,
                   sequence.getOutputs().size());

      Collection<ISequenceFlow> flows = sequence.getFlows();
      assertEquals("wrong number of flows!",
                   2,
                   flows.size());
      ISequenceFlow flow = Iterables.get(flows, 0);
      assertEquals("flow sequence number not correct!",
                   0,
                   flow.getSequenceNumber());
      assertFalse("flow should not have implementation!",
                  flow.getImplementation().isPresent());
      assertEquals("flow not correct!",
                   MessagingParadigm.PUBLISH_SUBSCRIBE,
                   flow.getMessagingFlow().getMessagingParadigm());
      assertEquals("inputs for flow not correct!",
                   Iterables.get(flow.getInputs(), 0),
                   systemTrackClassification);
      assertEquals("inputs for flow not correct!",
                   Iterables.get(flow.getOutputs(), 0),
                   trackPriority);

      flow = Iterables.get(flows, 1);
      assertEquals("flow sequence number not correct!",
                   0,
                   flow.getSequenceNumber());
      assertFalse("flow should not have implementation!",
                  flow.getImplementation().isPresent());
      assertEquals("flow not correct!",
                   MessagingParadigm.PUBLISH_SUBSCRIBE,
                   flow.getMessagingFlow().getMessagingParadigm());
      assertEquals("inputs for flow not correct!",
                   Iterables.get(flow.getInputs(), 0),
                   systemTrackClassification);
      assertEquals("inputs for flow not correct!",
                   Iterables.get(flow.getOutputs(), 0),
                   trackPriority);
   }
}
