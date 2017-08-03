package com.ngc.seaside.jellyfish.cli.command.report.requirementsverification;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;

import javax.json.JsonArray;
import javax.json.JsonString;
import javax.json.JsonValue;

import static org.apache.commons.lang.ArrayUtils.INDEX_NOT_FOUND;

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
   public static String substringBetween(final String str, final String open, final String close) {
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
}
