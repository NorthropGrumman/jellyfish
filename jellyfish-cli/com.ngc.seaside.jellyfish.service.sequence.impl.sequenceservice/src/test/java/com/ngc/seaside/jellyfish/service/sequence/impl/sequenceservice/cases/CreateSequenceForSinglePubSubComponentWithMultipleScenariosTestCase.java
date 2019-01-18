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

import com.ngc.seaside.jellyfish.service.scenario.api.MessagingParadigm;
import com.ngc.seaside.jellyfish.service.sequence.api.ISequence;
import com.ngc.seaside.jellyfish.service.sequence.api.ISequenceFlow;
import com.ngc.seaside.jellyfish.service.sequence.api.ISequenceService;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.ItTestState;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.Sequencing;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This test verifies the sequence of a simple pub/sub component with multiple scenarios yields two flows.
 */
public class CreateSequenceForSinglePubSubComponentWithMultipleScenariosTestCase {

   private final ItTestState state;

   /**
    * Creates a new test.
    */
   public CreateSequenceForSinglePubSubComponentWithMultipleScenariosTestCase(ItTestState state) {
      this.state = state;
   }

   /**
    * Runs the test.
    */
   public void execute() {
      IModel model = state.getSystemDescriptor()
            .findModel("com.ngc.seaside.engagementplanning.EngagementPlanner")
            .get();
      IDataReferenceField prioritizedSystemTracks = model.getInputs()
            .getByName("prioritizedSystemTracks")
            .get();
      IDataReferenceField droppedSystemTrack = model.getInputs()
            .getByName("droppedSystemTrack")
            .get();
      IDataReferenceField plan = model.getOutputs()
            .getByName("plan")
            .get();

      List<ISequence> sequences = state.getService().getSequences(
            state.getOptions(),
            model,
            ISequenceService.GenerationStrategy.EXACT,
            Arrays.asList(prioritizedSystemTracks, droppedSystemTrack));

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
      assertTrue("wrong inputs for sequence!",
                 Sequencing.equivalent(sequence.getInputs(),
                                       Arrays.asList(prioritizedSystemTracks, droppedSystemTrack)));
      assertTrue("wrong outputs for sequence!",
                 Sequencing.equivalent(sequence.getOutputs(),
                                       Arrays.asList(plan, plan)));

      Collection<ISequenceFlow> flows = sequence.getFlows();
      assertEquals("wrong number of flows!",
                   2,
                   flows.size());
      ISequenceFlow flow = Iterables.get(flows, 0);
      assertTrue("flow should have implementation!",
                 flow.getImplementation().isPresent());
      assertEquals("flow not correct!",
                   MessagingParadigm.PUBLISH_SUBSCRIBE,
                   flow.getMessagingFlow().getMessagingParadigm());
      assertEquals("inputs for flow not correct!",
                   Iterables.get(flow.getInputs(), 0),
                   prioritizedSystemTracks);
      assertEquals("inputs for flow not correct!",
                   Iterables.get(flow.getOutputs(), 0),
                   plan);

      flow = Iterables.get(flows, 1);
      assertFalse("flow should not have implementation!",
                  flow.getImplementation().isPresent());
      assertEquals("flow not correct!",
                   MessagingParadigm.PUBLISH_SUBSCRIBE,
                   flow.getMessagingFlow().getMessagingParadigm());
      assertEquals("inputs for flow not correct!",
                   Iterables.get(flow.getInputs(), 0),
                   droppedSystemTrack);
      assertEquals("inputs for flow not correct!",
                   Iterables.get(flow.getOutputs(), 0),
                   plan);
   }
}
