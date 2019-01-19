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
package com.ngc.seaside.jellyfish.cli.command.report.requirementsverification.utilities;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.traversal.ModelPredicates;
import com.ngc.seaside.systemdescriptor.model.api.traversal.Traversals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import javax.json.JsonArray;
import javax.json.JsonString;
import javax.json.JsonValue;

/**
 * Utility class for model and system descriptor related operation
 */
public class ModelUtils {

   private ModelUtils() {
   }

   /**
    * Retrieves all requirements from a given model
    *
    * @param model     model to search
    * @param searchKey keyword to search (e.g. satisfies)
    * @return the Collection of requirements found in the model
    */
   public static Collection<String> getRequirementsFromModel(IModel model, String searchKey) {
      IMetadata metadata = model.getMetadata();
      TreeSet<String> requirements = new TreeSet<>(Collections.reverseOrder());

      if (metadata != null) {
         requirements = getRequirementsFromMetadata(metadata, searchKey);
      }
      return requirements;
   }

   /**
    * Retrieves all requirements from a given scenario
    *
    * @param scenario  model to search
    * @param searchKey keyword to search (e.g. satisfies)
    * @return the Collection of requirements found in the scenario
    */
   public static Collection<String> getRequirementsFromScenario(IScenario scenario, String searchKey) {
      IMetadata metadata = scenario.getMetadata();
      TreeSet<String> requirements = new TreeSet<>(Collections.reverseOrder());

      if (metadata != null) {
         requirements = getRequirementsFromMetadata(metadata, searchKey);
      }
      return requirements;
   }

   /**
    * Retrieves all requirements from a given metadata
    *
    * @param metadata  model to search
    * @param searchKey keyword to search (e.g. satisfies)
    * @return the Collection of requirements found in the metadata
    */
   public static TreeSet<String> getRequirementsFromMetadata(IMetadata metadata, String searchKey) {
      JsonValue value = metadata.getJson().getOrDefault(searchKey, JsonValue.NULL);
      TreeSet<String> requirements = new TreeSet<>(Collections.reverseOrder());

      // Tolerate either a single value or an array of values.
      switch (value.getValueType()) {
         case ARRAY:
            JsonArray array = (JsonArray) value;
            for (int i = 0; i < array.size(); i++) {
               requirements.add(array.getString(i));
            }
            break;
         case STRING:
            requirements.add(((JsonString) value).getString());
            break;
         default:
      }
      return requirements;
   }

   /**
    * Returns a collection of models that matches the search criteria
    *
    * @param commandOptions Jellyfish command options containing system descriptor
    * @param values         the values in which to search
    * @param operator       the operator to apply to search
    */
   public static Collection<IModel> searchModels(IJellyFishCommandOptions commandOptions, String values,
                                                 String operator) {
      ISystemDescriptor sd = commandOptions.getSystemDescriptor();

      switch (operator) {
         case "AND":
            return Traversals.collectModels(sd, ModelPredicates.withAllStereotypes(valuesToCollection(values)));
         case "NOT":
            return Traversals.collectModels(sd, ModelPredicates.withAnyStereotype(valuesToCollection(values)).negate());
         default: // OR
            return Traversals.collectModels(sd, ModelPredicates.withAnyStereotype(valuesToCollection(values)));
      }
   }

   /**
    * Converts comma delimited values to a collection
    */
   private static Collection<String> valuesToCollection(String values) {
      List<String> valueCollection = new ArrayList<>();
      if (values.contains(",")) {
         valueCollection.addAll(Arrays.asList(values.split(",")));
      } else {
         valueCollection.add(values);
      }
      return valueCollection;
   }
}
