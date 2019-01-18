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

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.model.SequenceFlow;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.model.SequenceFlowImplementation;
import com.ngc.seaside.systemdescriptor.model.api.SystemDescriptors;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Base class for components that need to generate implementations of flows.
 */
public abstract class AbstractFlowImplementationGenerator {

   /**
    * Used for logging.
    */
   protected final ILogService logService;

   /**
    * Gets the declared scenarios of a model.
    */
   protected final IScenarioService scenarioService;

   /**
    * The options used to run Jellyfish.
    */
   protected IJellyFishCommandOptions options;

   /**
    * Creates a new generator.
    *
    * @param scenarioService the scenario service
    * @param logService      the log service
    */
   protected AbstractFlowImplementationGenerator(IScenarioService scenarioService,
                                                 ILogService logService) {
      this.scenarioService = scenarioService;
      this.logService = logService;
   }

   /**
    * Invoked to populate the given implementation object.  This will add the nested sequence flows to the
    * implementation.
    *
    * @param impl   the implementation object
    * @param model  the model that contains the flow
    * @param inputs the inputs to the flow.
    */
   protected void populateImpl(SequenceFlowImplementation impl,
                               IModel model,
                               Collection<IDataReferenceField> inputs) {
      Collection<IDataReferenceField> inputsToFlows = inputs;
      int flowSequenceNumber = 0;

      while (!inputsToFlows.isEmpty()) {
         inputsToFlows = populateImpl2(impl, model, inputsToFlows, flowSequenceNumber)
               .stream()
               .flatMap(f -> f.getOutputs().stream())
               .collect(Collectors.toList());
         flowSequenceNumber++;
      }
   }

   /**
    * Invoked recursively to populate a flor implementation.
    *
    * @param impl               the impl
    * @param model              the model that contains the flow
    * @param inputs             the inputs to the flow
    * @param flowSequenceNumber the sequence number of the current flow
    * @return the sequence of flows that make up the given impl
    */
   protected Collection<SequenceFlow> populateImpl2(SequenceFlowImplementation impl,
                                                    IModel model,
                                                    Collection<IDataReferenceField> inputs,
                                                    int flowSequenceNumber) {
      // Take the inputs to the flow and figure out which links reference those inputs.  This gives use a multimap where
      // the values are the links whose source reference one of the inputs.  The keys of the map are the targets of
      // each link.  So this gives use a way to find all the active, incoming links attached to a particular part field
      // or requirement field of the model that contains this flow that we need to investigate.
      Multimap<IModelReferenceField, IModelLink<IDataReferenceField>> componentsToExplore =
            getMappedLinksWhoseSourceIncludes(inputs, model);

      // There are all the new flows we have found.
      Collection<SequenceFlow> flows = new ArrayList<>();

      for (IModelReferenceField component : componentsToExplore.keySet()) {
         // The inputs to the flow generator are the targets of all the links going into the component. The values are
         // only the links we found in the loop above.
         Collection<IDataReferenceField> flowGenInputs = componentsToExplore.get(component).stream()
               .map(IModelLink::getTarget)
               .collect(Collectors.toList());

         // Use the flow generator to find flows.
         Collection<SequenceFlow> flowsForComponent = new FlowGenerator(scenarioService, logService)
               .generateFlows(new FlowGeneratorContext()
                                    .setModel(component.getType())
                                    .setSequenceNumber(flowSequenceNumber)
                                    .setOptions(options)
                                    .setInputs(flowGenInputs));

         // Add the discovered flows to a list that contains all the flows we have discovered so far.
         flows.addAll(flowsForComponent);

         // Register any new discoveries with the impl.
         for (SequenceFlow flowForComponent : flowsForComponent) {
            // Add the new flows to the impl.
            impl.addFlow(flowForComponent);

            // Add the links to the impl.
            // Note we do this in this inner loop because we need the component variable in scope to get the links
            // to register.
            componentsToExplore.get(component).forEach(link -> impl.addIncomingLink(flowForComponent, link));
            getLinksWhoseSourceIncludes(flowForComponent.getOutputs(), component, model)
                  .forEach(link -> impl.addOutgoingLink(flowForComponent, link));

            // Register the actual component associated with the flow.  We just need reference field of the target of
            // one of the links.  We have to have at least one link or we would not have gotten this far.
            IModelLink<IDataReferenceField> link = componentsToExplore.get(component).iterator().next();
            IModelReferenceField field = SystemDescriptors.getReferencedFieldOfLinkTarget(link).orElseThrow(
                  () -> new IllegalStateException("expected the link's target to reference a model! " + link));
            impl.addComponentImplementingFlow(flowForComponent, field);
         }
      }

      return flows;
   }

   /**
    * Gets the links declared in the given model which contain list the one of the given fields as as source.  Return
    * returned map is keyed on the part or requirement which is referenced in the target of each link.
    *
    * @param fields the fields
    * @param model  the model that contains the links
    * @return the links which reference the given fields in their sources
    */
   protected Multimap<IModelReferenceField, IModelLink<IDataReferenceField>> getMappedLinksWhoseSourceIncludes(
         Collection<IDataReferenceField> fields,
         IModel model) {
      Multimap<IModelReferenceField, IModelLink<IDataReferenceField>>
            componentsToExplore =
            LinkedListMultimap.create();

      for (IModelLink<?> link : model.getLinks()) {
         // We only care about the links linking data.  Make sure the source of the link is one of the inputs to the
         // flow.
         if (SystemDescriptors.isLinkToData(link) && fields.contains(link.getSource())) {
            @SuppressWarnings({"unchecked"})
            IModelLink<IDataReferenceField> casted = (IModelLink<IDataReferenceField>) link;
            // Keep track of which part or requirement this link is pointing to.  We'll use this later to compute the
            // flows inside the component.
            SystemDescriptors.getReferencedFieldOfLinkTarget(casted)
                  .ifPresent(field -> componentsToExplore.put(field, casted));
         }
      }

      return componentsToExplore;
   }

   /**
    * Gets the links declared in the given model which contain list the one of the given fields as as source.
    *
    * @param fields      the fields
    * @param sourceField the model field (a part of requirement) used to filter the returned links (only links whose
    *                    target points to this field are included)
    * @param model       the model that contains the links
    * @return the links which reference the given fields in their sources
    */
   protected Collection<IModelLink<IDataReferenceField>> getLinksWhoseSourceIncludes(
         Collection<IDataReferenceField> fields,
         IModelReferenceField sourceField,
         IModel model) {
      // This method is separate from the above because we don't care if the link is linking to a part or requirement
      // contained by the model (unlike the logic above).  In this case, we actually want to find any links that
      // reference the fields that might be connected directly to the output field(s) of the model.
      Collection<IModelLink<IDataReferenceField>> links = new ArrayList<>();

      for (IModelLink<?> link : model.getLinks()) {
         // We only care about the links linking data.  Make sure the source of the link is one of the inputs to the
         // flow.
         if (SystemDescriptors.isLinkToData(link)
                   && fields.contains(link.getSource())
                   // This check ensures that we only get links whose source field is referencing the given field
                   && SystemDescriptors.getReferencedFieldOfLinkSource(link).filter(sourceField::equals).isPresent()) {
            @SuppressWarnings({"unchecked"})
            IModelLink<IDataReferenceField> casted = (IModelLink<IDataReferenceField>) link;
            links.add(casted);
         }
      }

      return links;
   }
}
