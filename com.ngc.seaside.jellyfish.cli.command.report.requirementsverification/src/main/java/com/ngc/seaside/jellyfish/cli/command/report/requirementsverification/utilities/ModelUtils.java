package com.ngc.seaside.jellyfish.cli.command.report.requirementsverification.utilities;

import static org.apache.commons.lang.ArrayUtils.INDEX_NOT_FOUND;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.traversal.ModelPredicates;
import com.ngc.seaside.systemdescriptor.model.api.traversal.Traversals;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Pattern;

import javax.json.JsonArray;
import javax.json.JsonString;
import javax.json.JsonValue;

/**
 * Utility class for model and system descriptor related operation
 */
public class ModelUtils {

   private static final Pattern FEATURE_FILE_NAME_PATTERN = Pattern.compile("([a-zA-Z$_][a-zA-Z$_0-9]*[.]){2}feature");

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
    * <p>Gets the String that is nested in between two Strings. Only the first match is returned.</p>
    *
    * <p>A {@code null} input String returns {@code null}. A {@code null} open/close returns {@code null} (no match). An
    * empty ("") open and close returns an empty string.</p>
    *
    * @param str   the String containing the substring, may be null
    * @param open  the String before the substring, may be null
    * @param close the String after the substring, may be null
    * @return the substring, {@code null} if no match
    */
   private static String substringBetween(final String str, final String open, final String close) {
      if (str == null || open == null || close == null) {
         return null;
      }
      final int start = str.indexOf(open);
      if (start != INDEX_NOT_FOUND) {
         final int end = str.indexOf(close, start + open.length());
         if (end != INDEX_NOT_FOUND) {
            return str.substring(start + open.length(), end);
         }
      }
      return null;
   }

   /**
    * Resolves the feature files directory with the system descriptor location
    *
    * @param commandOptions Jellyfish command options containing system descriptor
    * @param uri            location of feature files relative to the system descriptor
    * @return resolved path to feature files directory
    */
   private static Path getResolvedFeatureFilesDirectory(IJellyFishCommandOptions commandOptions, String uri) {
      return commandOptions.getSystemDescriptorProjectPath().toAbsolutePath().resolve(uri);
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
