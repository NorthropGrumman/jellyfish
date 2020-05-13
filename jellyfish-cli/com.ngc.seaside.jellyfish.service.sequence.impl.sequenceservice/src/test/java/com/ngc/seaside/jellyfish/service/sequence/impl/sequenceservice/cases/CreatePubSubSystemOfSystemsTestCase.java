/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This test ensures that sequences are generated correctly for systems of systems.
 */
public class CreatePubSubSystemOfSystemsTestCase {

   private final ItTestState state;

   /**
    * Creates a new test.
    */
   public CreatePubSubSystemOfSystemsTestCase(ItTestState state) {
      this.state = state;
   }

   /**
    * Runs the test.
    */
   public void execute() {
      IModel ws = state.getSystemDescriptor().findModel("com.ngc.seaside.WeaponSystem").get();
      IModel threatEval = state.getSystemDescriptor().findModel("com.ngc.seaside.threateval.ThreatEvaluation").get();
      IModel ctps = state.getSystemDescriptor()
            .findModel("com.ngc.seaside.threateval.ClassificationTrackPriorityService").get();

      IDataReferenceField systemTrackClassification = ws.getInputs().getByName("systemTrackClassification").get();
      IDataReferenceField plan = ws.getOutputs().getByName("plan").get();
      IDataReferenceField teSystemTrackClassification =
            threatEval.getInputs().getByName("systemTrackClassification").get();
      IDataReferenceField tePrioritizedSystemTracks =
            threatEval.getOutputs().getByName("prioritizedSystemTracks").get();
      IDataReferenceField ctpsSystemTrackClassification = ctps.getInputs()
            .getByName("systemTrackClassification").get();
      IDataReferenceField ctpsTrackPriority = ctps.getOutputs()
            .getByName("trackPriority").get();

      List<ISequence> sequences = state.getService().getSequences(
            state.getOptions(),
            ws,
            ISequenceService.GenerationStrategy.EXACT,
            Collections.singleton(systemTrackClassification));
      assertEquals("did not generate the correct number of sequences!",
                   1,
                   sequences.size());

      ISequence sequence = sequences.get(0);
      assertEquals("sequence IDs should be sequential!",
                   1,
                   sequence.getId());
      assertEquals("model not correct!",
                   ws,
                   sequence.getModel());
      assertEquals("wrong number of inputs",
                   1,
                   sequence.getInputs().size());
      assertTrue("input for sequence not correct!",
                 sequence.getInputs().contains(systemTrackClassification));
      assertEquals("wrong number of outputs!",
                   1,
                   sequence.getOutputs().size());
      assertTrue("output for sequence not correct!",
                 sequence.getOutputs().contains(plan));
      assertEquals("wrong number of flows!",
                   1,
                   sequence.getFlows().size());

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
                   plan);

      assertTrue("flow should have implementation!",
                 flow.getImplementation().isPresent());

      ISequenceFlowImplementation impl = flow.getImplementation().get();
      assertEquals("impl should contain 2 flow!",
                   2,
                   impl.getFlows().size());

      flow = Iterables.get(impl.getFlows(), 0);
      assertEquals("flow not correct!",
                   MessagingParadigm.PUBLISH_SUBSCRIBE,
                   flow.getMessagingFlow().getMessagingParadigm());
      assertEquals("inputs for flow not correct!",
                   Iterables.get(flow.getInputs(), 0),
                   teSystemTrackClassification);
      assertEquals("outputs for flow not correct!",
                   Iterables.get(flow.getOutputs(), 0),
                   tePrioritizedSystemTracks);

      assertTrue("nested flow should have implementation!",
                 flow.getImplementation().isPresent());

      impl = flow.getImplementation().get();

      assertEquals("nested impl should contain 2 flow!",
                   2,
                   impl.getFlows().size());

      flow = Iterables.get(impl.getFlows(), 0);
      assertFalse("impl should not be present",
                  flow.getImplementation().isPresent());
      assertTrue("inputs to nested flow not correct!",
                 flow.getInputs().contains(ctpsSystemTrackClassification));
      assertTrue("outputs of nested flow not correct!",
                 flow.getOutputs().contains(ctpsTrackPriority));
   }
}
