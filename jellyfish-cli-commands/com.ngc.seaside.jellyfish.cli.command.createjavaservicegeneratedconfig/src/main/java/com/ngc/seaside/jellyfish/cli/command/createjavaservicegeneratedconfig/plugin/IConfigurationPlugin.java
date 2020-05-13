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
