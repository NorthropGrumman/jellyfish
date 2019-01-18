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
package com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.model.SequenceFlow;

/**
 * Configures the {@link FlowImplementationGenerator}.
 */
public class FlowImplementationGeneratorContext {

   /**
    * The flow to find the implementation for.
    */
   private SequenceFlow flow;

   /**
    * The options used to run Jellyfish.
    */
   private IJellyFishCommandOptions options;

   /**
    * Gets the flow to find the implementation for.
    *
    * @return the flow to find the implementation for
    */
   public SequenceFlow getFlow() {
      return flow;
   }

   /**
    * Sets the flow to find the implementation for
    *
    * @param flow the flow to find the implementation for
    * @return this context
    */
   public FlowImplementationGeneratorContext setFlow(SequenceFlow flow) {
      this.flow = flow;
      return this;
   }

   /**
    * Gets options used to run Jellyfish.
    *
    * @return options used to run Jellyfish
    */
   public IJellyFishCommandOptions getOptions() {
      return options;
   }

   /**
    * sets options used to run Jellyfish.
    *
    * @param options options used to run Jellyfish
    * @return this context
    */
   public FlowImplementationGeneratorContext setOptions(IJellyFishCommandOptions options) {
      this.options = options;
      return this;
   }
}
