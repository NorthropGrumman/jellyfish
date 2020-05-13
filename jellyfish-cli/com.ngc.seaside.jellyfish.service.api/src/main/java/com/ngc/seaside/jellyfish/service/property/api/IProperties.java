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
