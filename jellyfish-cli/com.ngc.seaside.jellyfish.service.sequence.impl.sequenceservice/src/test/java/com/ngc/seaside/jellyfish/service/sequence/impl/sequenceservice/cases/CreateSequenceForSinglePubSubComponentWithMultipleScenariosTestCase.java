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
