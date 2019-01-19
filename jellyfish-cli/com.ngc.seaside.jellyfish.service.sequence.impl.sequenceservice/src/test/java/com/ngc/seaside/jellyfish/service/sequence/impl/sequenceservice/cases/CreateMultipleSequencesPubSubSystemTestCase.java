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

import com.ngc.seaside.jellyfish.service.sequence.api.ISequence;
import com.ngc.seaside.jellyfish.service.sequence.api.ISequenceService;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.ItTestState;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This tests ensures that multiple sequences are generated correctly.
 */
public class CreateMultipleSequencesPubSubSystemTestCase {

   private final ItTestState state;

   /**
    * Creates a new test.
    */
   public CreateMultipleSequencesPubSubSystemTestCase(ItTestState state) {
      this.state = state;
   }

   /**
    * Runs the test.
    */
   public void execute() {
      IModel threatEval = state.getSystemDescriptor().findModel("com.ngc.seaside.threateval.ThreatEvaluation").get();
      IDataReferenceField systemTrack =
            threatEval.getInputs().getByName("systemTrack").get();
      IDataReferenceField systemTrackClassification =
            threatEval.getInputs().getByName("systemTrackClassification").get();
      IDataReferenceField trackEngagementStatus =
            threatEval.getInputs().getByName("trackEngagementStatus").get();
      IDataReferenceField impactAssessment =
            threatEval.getInputs().getByName("impactAssessment").get();

      List<ISequence> sequences = state.getService().getSequences(
            state.getOptions(),
            threatEval,
            ISequenceService.GenerationStrategy.ALL_COMBINATIONS,
            Arrays.asList(systemTrack,
                          systemTrackClassification,
                          trackEngagementStatus,
                          impactAssessment));
      assertEquals("did not generate the correct number of sequences!",
                   7,
                   sequences.size());
   }
}
