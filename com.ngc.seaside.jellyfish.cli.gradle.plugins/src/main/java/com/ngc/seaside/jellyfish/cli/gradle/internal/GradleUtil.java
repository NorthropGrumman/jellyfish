package com.ngc.seaside.jellyfish.cli.gradle.internal;

import org.gradle.api.ProjectConfigurationException;

import java.util.Map;

/**
 * Contains various utilities for dealing with Gradle projects.
 *
 * Note this class has been copied from seaside-gradle-plugins.  We do this to avoid a compile time dependency on those
 * plugins which can create downstream versioning problems.
 */
public class GradleUtil {

   private final static String MISSING_PROPERTY_ERROR_MESSAGE =
         "the property '%s' is not set!  Please ensure this property is set.  These type of properties"
         + " can be set in $GRADLE_USER_HOME/gradle.properties.  Note that $GRADLE_USER_HOME is not necessarily"
         + " the directory where Gradle is installed.  If $GRADLE_USER_HOME is not set, gradle.properties can"
         + " usually be found in $USER/.gradle/.  You can check which properties are set by running"
         + " 'gradle properties'.";

   private final static String
         MISSING_SYSTEM_PROPERTY_ERROR_MESSAGE =
         "the system property '%s' is not set!  Please ensure this property is set.  These types of properties can be"
         + " set in $GRADLE_USER_HOME/gradle.properties.  Prefix the name of the property with 'systemProp.'.  In this"
         + " case, use 'systemProp.%s'.  Note that $GRADLE_USER_HOME is not necessarily"
         + " the directory where Gradle is installed.  If $GRADLE_USER_HOME is not set, gradle.properties can"
         + " usually be found in $USER/.gradle/.  You can check which properties are set by running"
         + " 'gradle properties'.";

   private GradleUtil() {
   }

   /**
    * Requires that all the given properties are set, throwing a {@code ProjectConfigurationException} otherwise.
    *
    * @param properties    the properties set in the project
    * @param propertyName  the name of the property that must be set
    * @param propertyNames additional names of properties that must be set (optional)
    */
   public static void requireProperties(Map<String, ?> properties, String propertyName, String... propertyNames) {
      if (!properties.containsKey(propertyName)) {
         throw new ProjectConfigurationException(String.format(MISSING_PROPERTY_ERROR_MESSAGE, propertyName),
                                                 null);
      }

      if (propertyNames != null) {
         for (String name : propertyNames) {
            if (!properties.containsKey(name)) {
               throw new ProjectConfigurationException(String.format(MISSING_PROPERTY_ERROR_MESSAGE, name),
                                                       null);
            }
         }
      }
   }

   /**
    * Requires that all the given system properties are set, throwing a {@code ProjectConfigurationException}
    * otherwise.
    *
    * @param properties    the properties set in the project
    * @param propertyName  the name of the property that must be set
    * @param propertyNames additional names of properties that must be set (optional)
    */
   public static void requireSystemProperties(Map<String, ?> properties, String propertyName, String... propertyNames) {
      if (properties.containsKey(propertyName)) {
         throw new ProjectConfigurationException(String.format(MISSING_SYSTEM_PROPERTY_ERROR_MESSAGE,
                                                               propertyName,
                                                               propertyName),
                                                 null);
      }
      if (propertyNames != null) {
         for (String name : propertyNames) {
            if (!properties.containsKey(name)) {
               throw new ProjectConfigurationException(String.format(MISSING_SYSTEM_PROPERTY_ERROR_MESSAGE,
                                                                     name,
                                                                     name),
                                                       null);
            }
         }
      }
   }
}
