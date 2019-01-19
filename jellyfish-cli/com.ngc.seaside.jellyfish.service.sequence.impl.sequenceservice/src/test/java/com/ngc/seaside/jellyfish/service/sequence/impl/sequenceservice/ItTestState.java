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
package com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.sequence.api.ISequence;
import com.ngc.seaside.jellyfish.service.sequence.api.ISequenceFlow;
import com.ngc.seaside.jellyfish.service.sequence.api.ISequenceFlowImplementation;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.SystemDescriptors;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contains state for tests.
 */
public class ItTestState {

   private final SequenceService service;

   private final ISystemDescriptor systemDescriptor;

   private final ILogService logService;

   private final IJellyFishCommandOptions options;

   private final Deque<Integer> flowIndexes = new ArrayDeque<>();

   private int depth = 0;

   /**
    * Creates a new state.
    */
   public ItTestState(SequenceService service,
                      ISystemDescriptor systemDescriptor,
                      ILogService logService,
                      IJellyFishCommandOptions options) {
      this.service = service;
      this.systemDescriptor = systemDescriptor;
      this.logService = logService;
      this.options = options;
   }

   /**
    * Gets the sequence service.
    */
   public SequenceService getService() {
      return service;
   }

   /**
    * Gets the System Descripor.
    */
   public ISystemDescriptor getSystemDescriptor() {
      return systemDescriptor;
   }

   /**
    * Gets the log service.
    */
   public ILogService getLogService() {
      return logService;
   }

   /**
    * Gets the objects for Jellyfish.
    */
   public IJellyFishCommandOptions getOptions() {
      return options;
   }

   /**
    * Prints detailed info about a sequence to the log service.  Use a print stream log service to see this output.
    */
   public void debugDetailed(ISequence sequence) {
      logService.debug(getClass(), "");
      logService.debug(getClass(), "- Sequence %d begins", sequence.getId());
      logService.debug(getClass(), "  * This sequence is part of %s", sequence.getModel().getFullyQualifiedName());

      String msg = sequence.getInputs()
            .stream()
            .map(INamedChild::getName)
            .collect(Collectors.joining(", "));
      logService.debug(getClass(), "  * The following inputs have entered the sequence: %s", msg);
      logService.debug(getClass(), "  * This sequence has %d flows", sequence.getFlows().size());

      int flowIndex = 1;
      for (ISequenceFlow flow : sequence.getFlows()) {
         depth++;
         flowIndexes.push(flowIndex);
         debugDetailed(flow);
         flowIndexes.pop();
         depth--;

         flowIndex++;
      }

      msg = sequence.getOutputs()
            .stream()
            .map(INamedChild::getName)
            .collect(Collectors.joining(", "));
      logService.debug(getClass(), "  * The following outputs have exited the sequence: %s", msg);

      logService.debug(getClass(), "- Sequence %d ends%n", sequence.getId());
   }

   private void debugDetailed(ISequenceFlow flow) {
      int spaces = 2 + (depth * 2);

      logService.debug(getClass(), "%-" + spaces + "s+ Flow %s begins", "", flowId());

      logService.debug(getClass(),
                       "%-" + (spaces + 2) + "s- %s performs %s", "",
                       flow.getMessagingFlow().getScenario().getParent().getName(),
                       flow.getMessagingFlow().getScenario().getName());
      logService.debug(getClass(),
                       "%-" + (spaces + 2) + "s- This flow is consuming the inputs %s", "",
                       flow.getInputs().stream().map(INamedChild::getName).collect(Collectors.joining(", ")));

      logService.debug(getClass(),
                       "%-" + (spaces + 2) + "s- This flow %s", "",
                       flow.getImplementation().isPresent() ? "is implemented as follows:" : "is not implemented.");
      if (flow.getImplementation().isPresent()) {
         depth++;
         debugDetailed(flow.getImplementation().get());
         depth--;
      }

      logService.debug(getClass(),
                       "%-" + (spaces + 2) + "s- This flow is producing the outputs %s", "",
                       flow.getOutputs().stream().map(INamedChild::getName).collect(Collectors.joining(", ")));
      logService.debug(getClass(), "%-" + spaces + "s+ Flow %s ends", "", flowId());
   }

   private void debugDetailed(ISequenceFlowImplementation impl) {
      int spaces = 4 + (depth * 2);

      logService.trace(getClass(), "%-" + spaces + "s- This flow has %d nested flows.", "", impl.getFlows().size());

      int flowIndex = 1;
      for (ISequenceFlow flow : impl.getFlows()) {
         IModelReferenceField modelField = impl.getComponentImplementingFlow(flow);
         logService.trace(getClass(),
                          "%-" + spaces + "s- %s %s.%s performs the following flow:", "",
                          SystemDescriptors.isPart(modelField) ? "part" : "requirement",
                          modelField.getParent().getName(),
                          modelField.getName());

         depth++;
         flowIndexes.push(flowIndex);
         debugDetailed(flow);
         flowIndexes.pop();
         depth--;

         flowIndex++;
      }
   }

   private String flowId() {
      List<Integer> reversed = new ArrayList<>(flowIndexes);
      Collections.reverse(reversed);
      return reversed.stream().map(String::valueOf).collect(Collectors.joining("."));
   }
}
