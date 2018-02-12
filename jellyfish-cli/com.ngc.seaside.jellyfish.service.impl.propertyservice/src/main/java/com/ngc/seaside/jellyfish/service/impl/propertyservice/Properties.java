package com.ngc.seaside.jellyfish.service.impl.propertyservice;

import com.ngc.seaside.jellyfish.service.property.api.IProperties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of the IProperties interface.
 */
public class Properties implements IProperties {
   private final VelocityEngine engine = new VelocityEngine();
   private final VelocityContext context = new VelocityContext();
   private Map<String, String> original = new LinkedHashMap<>();
   private Map<String, String> current = new LinkedHashMap<>();

   /**
    * Constructor
    */
   public Properties() {
      engine.setProperty("runtime.references.strict", true);
      context.put("StringUtilities", StringUtilities.class);
   }

   /**
    * Load the properties given the properties file.
    *
    * @param propertiesFile the properties file.
    * @throws IOException if the file can't be parsed
    */
   void load(Path propertiesFile) throws IOException {
      original = parseFile(propertiesFile);
   }

   @Override
   public void put(String key, String value) {
     original.put(key, value);
   }

   @Override
   public String get(String key) {
      return current.get(key);
   }

   @Override
   public List<String> getKeys() {
      return new ArrayList<>(current.keySet());
   }

   @Override
   public void evaluate() throws IOException {
      //The interface states that the properties should be returned in the order in which they are
      //in the properties file. Using a LinkedHashMap will ensure iterating over the properties will work correctly.
      current = new LinkedHashMap<>();

      /**
       * Read each line of the properties file, evaluate it based on the previous lines context.
       * This allows you to use the previous line's value and manipulate it using the operations of the
       * java.util.String class.
       *
       * groupId=com.ngc.seaside
       * artifactId=mybundle
       * package=$groupId.$artifactId
       * capitalized=${package.toUpperCase()}
       */
      for(Map.Entry<String, String> entry : original.entrySet()) {
         StringWriter writer = new StringWriter();
         engine.evaluate(context,
                         writer,
                         String.format("parsing property %s with value %s",
                                       entry.getKey(), entry.getValue()),
                         entry.getValue());

         context.put(entry.getKey(), writer.toString());
         current.put(entry.getKey(), writer.toString());
      }
   }

   /**
    * Parse the properties file and return the map of properties. This map will contain the raw values and not the
    * modified version.
    *
    * @param propertiesFile the properties file.
    * @return the Map of raw values mapped to the property value.
    * @throws IOException
    */
   private Map<String, String> parseFile(Path propertiesFile) throws IOException {
      //The interface states that the properties should be returned in the order in which they are
      //in the properties file. Using a LinkedHashMap will ensure iterating over the properties will work correctly.
      Map<String, String> returnMap = new LinkedHashMap<>();
      for (String line : Files.readAllLines(propertiesFile)) {
         String trimmed = line.trim();
         if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {
            String[] values = trimmed.split("=");
            String name = values[0].trim();
            String value = values[1].trim();
            returnMap.put(name, value);
         }
      }
      return returnMap;
   }

}
