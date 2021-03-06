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
package com.ngc.seaside.jellyfish.cli.command.report.requirementsallocation.utilities;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.requirements.api.IRequirementsService;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.traversal.ModelPredicates;
import com.ngc.seaside.systemdescriptor.model.api.traversal.Traversals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

/**
 * Utility class for model and system descriptor related operation
 */
public class ModelUtils {

   private ModelUtils() {
   }

   /**
    * Returns a colletion of all requirements that a given model satisfies
    *
    * @param commandOptions      the command options
    * @param requirementsService the requirements service
    * @param model               the model to search for requirements
    * @return a collection of all requirements satisfied by the model
    */
   public static Collection<String> getAllRequirementsForModel(IJellyFishCommandOptions commandOptions,
                                                               IRequirementsService requirementsService, IModel model) {
      Collection<String> requirements = new TreeSet<>(Collections.reverseOrder());
      requirements.addAll(requirementsService.getRequirements(commandOptions, model));

      model.getInputs().forEach(item -> requirements.addAll(requirementsService.getRequirements(commandOptions, item)));
      model.getOutputs()
            .forEach(item -> requirements.addAll(requirementsService.getRequirements(commandOptions, item)));
      model.getParts().forEach(item -> requirements.addAll(requirementsService.getRequirements(commandOptions, item)));
      model.getScenarios()
            .forEach(item -> requirements.addAll(requirementsService.getRequirements(commandOptions, item)));

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
