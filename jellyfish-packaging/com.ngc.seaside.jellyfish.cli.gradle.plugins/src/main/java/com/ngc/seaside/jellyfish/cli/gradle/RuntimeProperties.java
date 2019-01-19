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
