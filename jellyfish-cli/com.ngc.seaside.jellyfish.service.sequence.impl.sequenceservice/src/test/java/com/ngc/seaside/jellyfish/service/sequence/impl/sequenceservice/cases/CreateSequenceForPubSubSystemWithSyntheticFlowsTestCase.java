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
import com.ngc.seaside.jellyfish.service.sequence.api.ISequenceFlowImplementation;
import com.ngc.seaside.jellyfish.service.sequence.api.ISequenceService;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.ItTestState;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test ensures that undeclared flows are actually detected.
 */
public class CreateSequenceForPubSubSystemWithSyntheticFlowsTestCase {

   private final ItTestState state;

   /**
    * Creates a new test.
    */
   public CreateSequenceForPubSubSystemWithSyntheticFlowsTestCase(ItTestState state) {
      this.state = state;
   }

   /**
    * Runs the test.
    */
   public void execute() {
      IModel ep = state.getSystemDescriptor().findModel("com.ngc.seaside.engagementplanning.EngagementPlanner2").get();
      IModel eps =
            state.getSystemDescriptor().findModel("com.ngc.seaside.engagementplanning.EngagementPlanningService").get();
      IDataReferenceField prioritizedSystemTracks = ep.getInputs().getByName("prioritizedSystemTracks").get();
      IDataReferenceField plan = ep.getOutputs().getByName("plan").get();
      IDataReferenceField epsPrioritizedSystemTracks = eps.getInputs().getByName("prioritizedSystemTracks").get();
      IDataReferenceField epsPlan = eps.getOutputs().getByName("plan").get();

      List<ISequence> sequences = state.getService().getSequences(
            state.getOptions(),
            ep,
            ISequenceService.GenerationStrategy.EXACT,
            Collections.singleton(prioritizedSystemTracks));
      sequences.forEach(state::debugDetailed);
      assertEquals("did not generate the correct number of sequences!",
                   1,
                   sequences.size());

      ISequence sequence = sequences.get(0);
      assertEquals("sequence IDs should be sequential!",
                   1,
                   sequence.getId());
      assertEquals("model not correct!",
                   ep,
                   sequence.getModel());
      assertEquals("wrong input for sequence!",
                   prioritizedSystemTracks,
                   Iterables.get(sequence.getInputs(), 0));
      assertEquals("wrong number of inputs",
                   1,
                   sequence.getInputs().size());
      assertEquals("wrong output for sequence!",
                   plan,
                   Iterables.get(sequence.getOutputs(), 0));
      assertEquals("wrong number of outputs!",
                   1,
                   sequence.getOutputs().size());

      ISequenceFlow flow = Iterables.get(sequence.getFlows(), 0);
      assertEquals("flow sequence number not correct!",
                   0,
                   flow.getSequenceNumber());
      assertEquals("flow not correct!",
                   MessagingParadigm.PUBLISH_SUBSCRIBE,
                   flow.getMessagingFlow().getMessagingParadigm());
      assertEquals("inputs for flow not correct!",
                   Iterables.get(flow.getInputs(), 0),
                   prioritizedSystemTracks);
      assertEquals("inputs for flow not correct!",
                   Iterables.get(flow.getOutputs(), 0),
                   plan);

      assertTrue("flow should have implementation!",
                 flow.getImplementation().isPresent());

      ISequenceFlowImplementation impl = flow.getImplementation().get();
      assertEquals("impl should contain 1 flow!",
                   1,
                   impl.getFlows().size());
      assertEquals("parent flow not correct!",
                   flow,
                   impl.getParentFlow());
      assertTrue("missing incoming link!",
                 impl.getIncomingLinks().contains(ep.getLinkByName("a").get()));
      assertEquals("wrong number of incoming links!",
                   1,
                   impl.getIncomingLinks().size());
      assertTrue("missing outgoing link!",
                 impl.getOutgoingLinks().contains(ep.getLinkByName("b").get()));
      assertEquals("wrong number of outgoing links!",
                   1,
                   impl.getOutgoingLinks().size());

      flow = Iterables.get(impl.getFlows(), 0);
      assertEquals("flow not correct!",
                   MessagingParadigm.PUBLISH_SUBSCRIBE,
                   flow.getMessagingFlow().getMessagingParadigm());
      assertEquals("inputs for flow not correct!",
                   Iterables.get(flow.getInputs(), 0),
                   epsPrioritizedSystemTracks);
      assertEquals("outputs for flow not correct!",
                   Iterables.get(flow.getOutputs(), 0),
                   epsPlan);

      assertEquals("incoming link mapped to input not correct!",
                   ep.getLinkByName("a").get(),
                   impl.getIncomingLink(flow, epsPrioritizedSystemTracks));
      assertEquals("outgoing link mapped to output not correct!",
                   ep.getLinkByName("b").get(),
                   impl.getOutgoingLink(flow, epsPlan));
   }
}
