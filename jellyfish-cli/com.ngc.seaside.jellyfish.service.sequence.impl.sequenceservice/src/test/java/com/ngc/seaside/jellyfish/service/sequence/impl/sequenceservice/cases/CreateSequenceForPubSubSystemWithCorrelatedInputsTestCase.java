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
import com.ngc.seaside.jellyfish.service.sequence.api.ISequenceFlowImplementation;
import com.ngc.seaside.jellyfish.service.sequence.api.ISequenceService;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.ItTestState;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test uses the Threat Evaluation system.  The resulting sequence contains a flow whose implementation uses
 * correlation.
 */
public class CreateSequenceForPubSubSystemWithCorrelatedInputsTestCase {

   private final ItTestState state;

   /**
    * Creates a new test.
    */
   public CreateSequenceForPubSubSystemWithCorrelatedInputsTestCase(ItTestState state) {
      this.state = state;
   }

   /**
    * Runs the test.
    */
   public void execute() {
      IModel threatEval = state.getSystemDescriptor()
            .findModel("com.ngc.seaside.threateval.ThreatEvaluation")
            .get();
      IDataReferenceField systemTrack = threatEval.getInputs().getByName("systemTrack").get();
      IDataReferenceField impactAssessment = threatEval.getInputs().getByName("impactAssessment").get();
      IDataReferenceField prioritizedSystemTracks = threatEval.getOutputs().getByName("prioritizedSystemTracks").get();

      IModel datps = state.getSystemDescriptor()
            .findModel("com.ngc.seaside.threateval.DefendedAreaTrackPriorityService")
            .get();
      IDataReferenceField datpsSystemTrack = datps.getInputs().getByName("systemTrack").get();
      IDataReferenceField datpsImpactAssessment = datps.getInputs().getByName("impactAssessment").get();
      IDataReferenceField datpsTrackPriority = datps.getOutputs().getByName("trackPriority").get();

      List<ISequence> sequences = state.getService().getSequences(state.getOptions(),
                                                                  threatEval,
                                                                  ISequenceService.GenerationStrategy.EXACT,
                                                                  Arrays.asList(systemTrack, impactAssessment));
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
                 sequence.getInputs().contains(systemTrack));
      assertTrue("input for sequence not correct!",
                 sequence.getInputs().contains(impactAssessment));
      assertEquals("wrong number of outputs!",
                   1,
                   sequence.getOutputs().size());
      assertTrue("output for sequence not correct!",
                 sequence.getOutputs().contains(prioritizedSystemTracks));

      ISequenceFlow flow = Iterables.get(sequence.getFlows(), 0);
      assertEquals("flow sequence number not correct!",
                   0,
                   flow.getSequenceNumber());
      assertEquals("flow not correct!",
                   MessagingParadigm.PUBLISH_SUBSCRIBE,
                   flow.getMessagingFlow().getMessagingParadigm());
      assertTrue("inputs for flow not correct!",
                 flow.getInputs().contains(impactAssessment));
      assertTrue("inputs for flow not correct!",
                 flow.getInputs().contains(systemTrack));
      assertEquals("inputs for flow not correct!",
                   flow.getInputs().size(),
                   2);
      assertTrue("outputs for flow not correct!",
                 flow.getOutputs().contains(prioritizedSystemTracks));
      assertEquals("outputs for flow not correct!",
                   flow.getOutputs().size(),
                   1);

      assertTrue("flow should have implementation!",
                 flow.getImplementation().isPresent());

      ISequenceFlowImplementation impl = flow.getImplementation().get();
      assertEquals("impl should contain 2 flows!",
                   2,
                   impl.getFlows().size());
      assertEquals("parent flow not correct!",
                   flow,
                   impl.getParentFlow());
      assertTrue("missing incoming link!",
                 impl.getIncomingLinks().contains(threatEval.getLinkByName("a").get()));
      assertTrue("missing incoming link!",
                 impl.getIncomingLinks().contains(threatEval.getLinkByName("b").get()));
      assertEquals("wrong number of incoming links!",
                   2,
                   impl.getIncomingLinks().size());
      assertTrue("missing outgoing link!",
                 impl.getOutgoingLinks().contains(threatEval.getLinkByName("f").get()));
      assertEquals("wrong number of outgoing links!",
                   1,
                   impl.getOutgoingLinks().size());

      flow = Iterables.get(impl.getFlows(), 0);
      assertEquals("flow sequence number not correct!",
                   0,
                   flow.getSequenceNumber());
      assertEquals("flow not correct!",
                   MessagingParadigm.PUBLISH_SUBSCRIBE,
                   flow.getMessagingFlow().getMessagingParadigm());
      assertTrue("inputs to flow not correct!",
                 flow.getInputs().contains(datpsSystemTrack));
      assertTrue("inputs to flow not correct!",
                 flow.getInputs().contains(datpsImpactAssessment));

      assertEquals("incoming link mapped to input not correct!",
                   threatEval.getLinkByName("a").get(),
                   impl.getIncomingLink(flow, datpsSystemTrack));
      assertEquals("incoming link mapped to input not correct!",
                   threatEval.getLinkByName("b").get(),
                   impl.getIncomingLink(flow, datpsImpactAssessment));
      assertEquals("outgoing link mapped to output not correct!",
                   threatEval.getLinkByName("g").get(),
                   impl.getOutgoingLink(flow, datpsTrackPriority));
   }
}
