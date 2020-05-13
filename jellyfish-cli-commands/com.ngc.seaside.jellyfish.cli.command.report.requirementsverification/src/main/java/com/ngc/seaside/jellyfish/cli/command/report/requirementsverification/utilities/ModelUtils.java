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
