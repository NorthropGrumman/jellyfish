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

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.scenario.api.IMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IRequestResponseMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.jellyfish.service.sequence.api.ISequenceFlowImplementation;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.model.SyntheticMessagingFlow;
import com.ngc.seaside.systemdescriptor.model.api.SystemDescriptors;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

/**
 * Contains various utilities for dealing with sequences.
 */
public class Sequencing {

   private Sequencing() {
   }

   /**
    * Returns true if both collections contain the same entries regardless of order and regardless of the type of each
    * collection.
    *
    * @param collectionA the first collection
    * @param collectionB the second collection
    * @param <T>         the type of entries the collections contain
    * @return true if the collections contain the same entries
    */
   public static <T> boolean equivalent(Collection<T> collectionA, Collection<T> collectionB) {
      return collectionA.size() == collectionB.size() && collectionA.containsAll(collectionB);
   }

   /**
    * Returns true if both collections contain the same entries regardless of order and regardless of the type of each
    * collection using the given predicate to compare the entries.
    *
    * @param collectionA the first collection
    * @param collectionB the second collection
    * @param predicate   the comparator to compare entries with
    * @param <T>         the type of entries the collections contain
    * @return true if the collections contain the same entries
    */
   public static <T> boolean equivalent(Collection<T> collectionA,
                                        Collection<T> collectionB,
                                        BiPredicate<T, T> predicate) {
      if (collectionA.size() != collectionB.size()) {
         return false;
      }
      for (T entryA : collectionA) {
         if (collectionB.stream().noneMatch(entryB -> predicate.test(entryA, entryB))) {
            return false;
         }
      }
      return true;
   }

   /**
    * Returns true if both maps contain the same mappings regardless of order and regardless of the type of each
    * map using the given predicates to compare the keys and values.
    *
    * @param mapA           the first map
    * @param mapB           the second map
    * @param keyPredicate   the comparator to compare keys with
    * @param valuePredicate the comparator to compare values with
    * @param <K>            the type of the keys in the maps
    * @param <V>            the type of the values in the maps
    * @return true if the maps contain the same mappings
    */
   public static <K, V> boolean equivalent(Map<K, V> mapA,
                                           Map<K, V> mapB,
                                           BiPredicate<K, K> keyPredicate,
                                           BiPredicate<V, V> valuePredicate) {
      if (mapA.size() != mapB.size()) {
         return false;
      }
      for (Map.Entry<K, V> entryA : mapA.entrySet()) {
         if (mapB.entrySet().stream().noneMatch(
               entryB -> keyPredicate.test(entryA.getKey(), entryB.getKey())
                         && valuePredicate.test(entryA.getValue(), entryB.getValue()))) {
            return false;
         }
      }
      return true;
   }

   /**
    * Returns true if the {@code linkA} is connected to {@code linkB}.  That is, the target field of {@code linkA} is
    * the source field of {@code linkB}.
    *
    * @param linkA the first link
    * @param linkB the second link
    * @param <T>   the type of fields the links reference
    * @return true if {@code linkA} connects to {@code linkB}
    */
   public static <T extends IReferenceField> boolean areLinksConnected(IModelLink<T> linkA, IModelLink<T> linkB) {
      return linkA.getTarget().equals(linkB.getSource());
   }

   /**
    * Gets all the flows that are contained by the given scenario.
    *
    * @param scenarioService the scenario service
    * @param options         the options Jellyfish was invoked with
    * @param scenario        the scenario to get the flows for
    * @return the flows that the scenario contains
    */
   public static Collection<IMessagingFlow> getFlows(IScenarioService scenarioService,
                                                     IJellyFishCommandOptions options,
                                                     IScenario scenario) {
      Collection<IMessagingFlow> flows = new ArrayList<>();
      scenarioService.getPubSubMessagingFlow(options, scenario).ifPresent(flows::add);
      scenarioService.getRequestResponseMessagingFlow(options, scenario).ifPresent(flows::add);
      return flows;
   }

   /**
    * Gets the inputs to the flow regardless of the type of flow.
    *
    * @param flow the flow to get the inputs for
    * @return the inputs to the flow
    */
   public static Collection<IDataReferenceField> getInputs(IMessagingFlow flow) {
      Collection<IDataReferenceField> inputs = new ArrayList<>();
      if (flow instanceof SyntheticMessagingFlow) {
         inputs.addAll(((SyntheticMessagingFlow) flow).getInputs());
      } else {
         switch (flow.getMessagingParadigm()) {
            case PUBLISH_SUBSCRIBE:
               inputs.addAll(((IPublishSubscribeMessagingFlow) flow).getInputs());
               break;
            case REQUEST_RESPONSE:
               inputs.add(((IRequestResponseMessagingFlow) flow).getInput());
               break;
            default:
               // Ignore unknown types.
               break;
         }
      }
      return inputs;
   }

   /**
    * Gets the outputs for the flow regardless of the type flow.
    *
    * @param flow the flow to get the outputs for
    * @return the outputs of the flow
    */
   public static Collection<IDataReferenceField> getOutputs(IMessagingFlow flow) {
      Collection<IDataReferenceField> outputs = new ArrayList<>();
      if (flow instanceof SyntheticMessagingFlow) {
         outputs.addAll(((SyntheticMessagingFlow) flow).getOutputs());
      } else {
         switch (flow.getMessagingParadigm()) {
            case PUBLISH_SUBSCRIBE:
               outputs.addAll(((IPublishSubscribeMessagingFlow) flow).getOutputs());
               break;
            case REQUEST_RESPONSE:
               outputs.add(((IRequestResponseMessagingFlow) flow).getOutput());
               break;
            default:
               // Ignore unknown types.
               break;
         }
      }
      return outputs;
   }

   /**
    * Gets the outputs of the given flow implementation.
    *
    * @param flowImpl the flow implementation
    * @return the output fields of the flow implementation (these are fields declared in the model that contains the
    * flow implementation)
    */
   public static Collection<IDataReferenceField> getOutputs(ISequenceFlowImplementation flowImpl) {
      return flowImpl.getOutgoingLinks()
            .stream()
            .map(IModelLink::getTarget) // We just want the targets of the links.
            .collect(Collectors.toList());
   }

   /**
    * Gets the links declared in the model whose sources point to one of the given inputs.  A multimap is returned whose
    * keys are the part or requirement of {@code model} which corresponding link references as a target.  The values
    * are the links themselves.
    *
    * @param model  the model that contains the links
    * @param inputs the input fields (these fields are declared in the given model)
    * @return a multimap which contains the links whose sources are included in the given inputs
    */
   @SuppressWarnings({"unchecked"})
   public static Multimap<IModelReferenceField, IModelLink<IDataReferenceField>> getLinksReferencingInputs(
         IModel model,
         Collection<IDataReferenceField> inputs) {
      Collection<IModelLink<IDataReferenceField>> linksReferencingInputs = model.getLinks()
            .stream()
            // Find links that list any of the inputs in the current scope as a source.
            .filter(l -> inputs.contains(l.getSource()))
            // By the above filter, we know that the resulting links must link data together.
            .map(l -> (IModelLink<IDataReferenceField>) l)
            .collect(Collectors.toList());

      // Group the links by the component (either a part or requirement field) the target of each link points to.
      Multimap<IModelReferenceField, IModelLink<IDataReferenceField>> linksByReferencedField =
            LinkedListMultimap.create();
      for (IModelLink<IDataReferenceField> link : linksReferencingInputs) {
         // Get the field the target of the link references.  Note we require the link to have an expression.  It must
         // must have an expression as a target if the source of the link is a field of the model.
         IModelReferenceField targetField = SystemDescriptors
               .getReferencedFieldOfLinkTarget(link)
               .orElseThrow(() -> linkWithSourceButNoTarget(link));
         linksByReferencedField.put(targetField, link);
      }

      return linksByReferencedField;
   }

   private static RuntimeException linkWithSourceButNoTarget(IModelLink<?> link) {
      String msg = String.format(
            "the link '%s' of '%s' accepts data but its target does not reference a field of the model!",
            link.getName().orElse("unnamed"),
            link.getParent().getFullyQualifiedName());
      return new RuntimeException(msg);
   }
}
