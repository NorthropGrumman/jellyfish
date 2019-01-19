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

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This test verifies a sequence has a flow with an implementation which is really implemented twice.
 */
public class CreateSequenceForPubSubSystemWithDuplicateFlowImplTestCase {

   private final ItTestState state;

   /**
    * Creates a new test.
    */
   public CreateSequenceForPubSubSystemWithDuplicateFlowImplTestCase(ItTestState state) {
      this.state = state;
   }

   /**
    * Runs the test.
    */
   public void execute() {
      IModel outerSystem =
            state.getSystemDescriptor().findModel("com.ngc.seaside.threateval.OuterSystemDuplicate").get();
      IDataReferenceField systemTrackClassification =
            outerSystem.getInputs().getByName("systemTrackClassification").get();
      IDataReferenceField trackPriority =
            outerSystem.getOutputs().getByName("trackPriority").get();

      IModel ctps = outerSystem.getParts().getByName("classificationTrackPriorityService").get().getType();
      IDataReferenceField ctpsSystemTrackClassification =
            ctps.getInputs().getByName("systemTrackClassification").get();
      IDataReferenceField ctpsTrackPriority =
            ctps.getOutputs().getByName("trackPriority").get();

      List<ISequence> sequences = state.getService().getSequences(
            state.getOptions(),
            outerSystem,
            ISequenceService.GenerationStrategy.EXACT,
            Arrays.asList(systemTrackClassification, systemTrackClassification));
      sequences.forEach(state::debugDetailed);
      assertEquals("did not generate the correct number of sequences!",
                   1,
                   sequences.size());

      ISequence sequence = sequences.get(0);
      assertEquals("sequence IDs should be sequential!",
                   1,
                   sequence.getId());
      assertEquals("model not correct!",
                   outerSystem,
                   sequence.getModel());
      assertEquals("wrong input for sequence!",
                   systemTrackClassification,
                   Iterables.get(sequence.getInputs(), 0));
      assertEquals("wrong number of inputs",
                   2,
                   sequence.getInputs().size());
      assertEquals("wrong output for sequence!",
                   trackPriority,
                   sequence.getOutputs().stream().distinct().findFirst().get());
      assertEquals("wrong number of outputs!",
                   6,
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
                   systemTrackClassification);
      assertEquals("inputs for flow not correct!",
                   Iterables.get(flow.getOutputs(), 0),
                   trackPriority);

      // The declared flow should not be implemented because it is actually implemented twice.  In this case,
      // two trackPriorities outputs will be produced.  We consider this an undeclared flow which takes a single input
      // (systemTrackClassification) but produces two outputs (trackPriority and trackPriority).
      assertFalse("flow should not implementation!",
                  flow.getImplementation().isPresent());

      // The last flow happened twice.

      // The next flow should be a synthetic flow that takes a single input (systemTrackClassification) and produces
      // two outputs (trackPriority and trackPriority).
      flow = Iterables.get(sequence.getFlows(), 3);
      assertEquals("flow sequence number not correct!",
                   0,
                   flow.getSequenceNumber());
      assertEquals("flow not correct!",
                   MessagingParadigm.PUBLISH_SUBSCRIBE,
                   flow.getMessagingFlow().getMessagingParadigm());
      assertEquals("inputs for flow not correct!",
                   Iterables.get(flow.getInputs(), 0),
                   systemTrackClassification);
      assertEquals("outputs for flow not correct!",
                   Iterables.get(flow.getOutputs(), 0),
                   trackPriority);
      assertEquals("outputs for flow not correct!",
                   Iterables.get(flow.getOutputs(), 1),
                   trackPriority);

      assertTrue("flow should be implementation!",
                 flow.getImplementation().isPresent());

      ISequenceFlowImplementation impl = flow.getImplementation().get();
      assertEquals("impl should contain 2 flows!",
                   2,
                   impl.getFlows().size());
      assertEquals("parent flow not correct!",
                   flow,
                   impl.getParentFlow());
      assertTrue("missing incoming link!",
                 impl.getIncomingLinks().contains(outerSystem.getLinkByName("a").get()));
      assertTrue("missing incoming link!",
                 impl.getIncomingLinks().contains(outerSystem.getLinkByName("c").get()));
      assertEquals("wrong number of incoming links!",
                   2,
                   impl.getIncomingLinks().size());
      assertTrue("missing outgoing link!",
                 impl.getOutgoingLinks().contains(outerSystem.getLinkByName("b").get()));
      assertTrue("missing outgoing link!",
                 impl.getOutgoingLinks().contains(outerSystem.getLinkByName("d").get()));
      assertEquals("wrong number of outgoing links!",
                   2,
                   impl.getOutgoingLinks().size());

      flow = Iterables.get(impl.getFlows(), 0);
      assertEquals("flow not correct!",
                   MessagingParadigm.PUBLISH_SUBSCRIBE,
                   flow.getMessagingFlow().getMessagingParadigm());
      assertEquals("inputs for flow not correct!",
                   Iterables.get(flow.getInputs(), 0),
                   ctpsSystemTrackClassification);
      assertEquals("outputs for flow not correct!",
                   Iterables.get(flow.getOutputs(), 0),
                   ctpsTrackPriority);

      assertEquals("incoming link mapped to input not correct!",
                   outerSystem.getLinkByName("a").get(),
                   impl.getIncomingLink(flow, ctpsSystemTrackClassification));
      assertEquals("outgoing link mapped to output not correct!",
                   outerSystem.getLinkByName("b").get(),
                   impl.getOutgoingLink(flow, ctpsTrackPriority));

      flow = Iterables.get(impl.getFlows(), 1);
      assertEquals("flow not correct!",
                   MessagingParadigm.PUBLISH_SUBSCRIBE,
                   flow.getMessagingFlow().getMessagingParadigm());
      assertEquals("inputs for flow not correct!",
                   Iterables.get(flow.getInputs(), 0),
                   ctpsSystemTrackClassification);
      assertEquals("outputs for flow not correct!",
                   Iterables.get(flow.getOutputs(), 0),
                   ctpsTrackPriority);

      assertEquals("incoming link mapped to input not correct!",
                   outerSystem.getLinkByName("c").get(),
                   impl.getIncomingLink(flow, ctpsSystemTrackClassification));
      assertEquals("outgoing link mapped to output not correct!",
                   outerSystem.getLinkByName("d").get(),
                   impl.getOutgoingLink(flow, ctpsTrackPriority));
   }
}
