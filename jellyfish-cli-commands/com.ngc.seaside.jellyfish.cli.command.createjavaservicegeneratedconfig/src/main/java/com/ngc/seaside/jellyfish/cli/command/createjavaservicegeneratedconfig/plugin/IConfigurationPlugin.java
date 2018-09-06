/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin;

import java.util.Set;

/**
 * An interface for adding to a generated configuration project. Creating a new plugin entails developing a
 * {@link #getTemplate() template} that will generate the configuration classes, and creating a
 * {@link #getConfigurationDto(ConfigurationContext) dto} type that the template will use.
 */
public interface IConfigurationPlugin {

   /**
    * Returns {@code true} if the plugin should be used given the context.
    * 
    * @param context context
    * @return {@code true} if the plugin should be used given the context
    */
   boolean isValid(ConfigurationContext context);

   /**
    * Returns the dependencies for the configuration in the form {@code "groupId:artifactId"}.
    * 
    * @param context context for configuring the plugin
    * @param dependencyType the type of dependencies to return
    * @return the dependencies for this transport configuration
    */
   Set<String> getDependencies(ConfigurationContext context, DependencyType dependencyType);

   /**
    * Enum for the type of dependency.
    */
   enum DependencyType {
      /**
       * Dependencies needed to compile the generated plugin configuration.
       */
      COMPILE,

      /**
       * Default bundle dependencies needed to satisfy all component references for the generated plugin configuration.
       */
      BUNDLE,

      /**
       * Default Guice module dependencies needed to satisfy all injections for the generated plugin configuration.
       */
      MODULE
   }
}
