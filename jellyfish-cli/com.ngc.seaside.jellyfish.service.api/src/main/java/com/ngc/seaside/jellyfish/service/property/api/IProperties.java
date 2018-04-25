package com.ngc.seaside.jellyfish.service.property.api;

import java.io.IOException;
import java.util.List;

/**
 * The properties parsed from a property file. The properties are evaluated based on dynamic
 * property substitution and manipulation.
 * groupId=com.ngc.seaside
 * artifactId=mybundle
 * package=$groupId.$artifactId
 * capitalized=${package.toUpperCase()}
 * Any operation that a java.util.String can perform can be set in the property file and called on the property
 * A more elaborate example below will replace all the periods in a package with a - and convert it to upper case.<br>
 * ${package.trim().toUpperCase().replace(".", "-")}
 */
public interface IProperties {

   /**
    * Call this method if you have used the {@link #put(String, String)} and need to re-evaluate the
    * properties based on the new value.
    *
    * @throws IOException if the properties are invalid.
    */
   void evaluate() throws IOException;

   /**
    * Add or replace a value in the properties.
    *
    * @param key   the key.
    * @param value the value.
    */
   void put(String key, String value);

   /**
    * Get the value given its key.
    *
    * @param key the key.
    * @return the value or null if not found.
    */
   String get(String key);

   /**
    * Get the list of keys in the order in which they were added.
    *
    * @return the keys.
    */
   List<String> getKeys();
}
