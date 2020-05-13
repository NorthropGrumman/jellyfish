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
package com.ngc.seaside.jellyfish.cli.gradle;

import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Contains properties are generated during the Gradle build and used at runtime when the Gradle plugins are actually
 * applied.
 */
public class RuntimeProperties {

   /**
    * The name of the properties file that is loaded at runtime.
    */
   private static final String CONFIG_PROPERTIES_FILE = "com.ngc.seaside.jellyfish.cli.gradle.config.properties";

   /**
    * The name of the property that identifies the version of Jellyfish currently in use.
    */
   private static final String VERSION_PROPERTY = "version";

   /**
    * The name of the property that identifies the group ID of the Jellyfish artifacts.
    */
   private static final String GROUP_PROPERTY = "group";

   /**
    * The version of Jellyfish in use (may be {@code null} if not yet initialized).
    */
   private static String version;

   /**
    * The Group ID of Jellyfish artifacts (may be {@code null} if not yet initialized).
    */
   private static String group;

   /**
    * Gets the version of Jellyfish currently be used.
    *
    * @return the version of Jellyfish currently be used
    */
   public static synchronized String getVersion() {
      if (version == null) {
         loadProperties();
      }
      return version;
   }

   /**
    * Gets the group ID for all Jellyfish artifacts.
    *
    * @return the group ID for all Jellyfish artifacts
    */
   public static synchronized String getGroup() {
      if (group == null) {
         loadProperties();
      }
      return group;
   }

   private static void loadProperties() {
      try (InputStream is = RuntimeProperties.class.getClassLoader()
            .getResourceAsStream(CONFIG_PROPERTIES_FILE)) {
         Properties props = new Properties();
         props.load(is);
         version = props.getProperty(VERSION_PROPERTY);
         Preconditions.checkState(version != null,
                                  "property '%s' not set in configuration properties!",
                                  VERSION_PROPERTY);
         group = props.getProperty(GROUP_PROPERTY);
         Preconditions.checkState(group != null,
                                  "property '%s' not set in configuration properties!",
                                  GROUP_PROPERTY);
      } catch (IOException e) {
         throw new IllegalStateException("failed to load configuration properties from classpath!", e);
      }
   }
}
