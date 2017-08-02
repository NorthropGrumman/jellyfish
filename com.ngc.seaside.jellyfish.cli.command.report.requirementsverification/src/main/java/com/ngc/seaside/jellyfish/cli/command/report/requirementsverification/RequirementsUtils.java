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

public class RequirementsUtils {
   private RequirementsUtils() {
   }

   public static Collection<String> getRequirementsFromModel(IModel model, String searchKey) {
      IMetadata metadata = model.getMetadata();
      TreeSet<String> requirements = new TreeSet<>(Collections.reverseOrder());

      if (metadata != null) {
         requirements = getRequirementsFromMetadata(metadata, searchKey);
      }
      return requirements;
   }

   public static Collection<String> getRequirementsFromScenario(IScenario scenario, String searchKey) {
      IMetadata metadata = scenario.getMetadata();
      TreeSet<String> requirements = new TreeSet<>(Collections.reverseOrder());

      if (metadata != null) {
         requirements = getRequirementsFromMetadata(metadata, searchKey);
      }
      return requirements;
   }

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
