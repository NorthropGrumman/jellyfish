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

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * An interface for adding to a generated configuration project. Creating a new plugin entails developing a
 * {@link #getTemplate() template} that will generate the configuration classes, and creating a
 * {@link #getConfigurationDto(ConfigurationContext) dto} type that the template will use.
 *
 * @param <T> the type of the dto for generating the configuration template
 */
public interface IConfigurationTemplatePlugin<T> extends IConfigurationPlugin {

   @Override
   default boolean isValid(ConfigurationContext context) {
      return getConfigurationDto(context).isPresent();
   }
   
   /**
    * Gets the dto for the plugin configuration, or {@link Optional#empty()} if this plugin should not be used.
    *
    * <p />
    * When generating the template for this plugin configuration,
    * The template will be given the returned {@code dto}.
    *
    * @param context context for configuring the plugin
    * @return the dto for the plugin configuration
    */
   Optional<T> getConfigurationDto(ConfigurationContext context);

   /**
    * Returns a map of extra parameters needed to generate the template. The parameter {@code dto} will already be
    * included using what is returned by {@link #getConfigurationDto}.
    *
    * @param context context for configuring the plugin
    * @return a map of extra parameters needed to generate the template
    */
   default Map<String, Object> getExtraTemplateParameters(ConfigurationContext context) {
      return Collections.emptyMap();
   }

   /**
    * Returns the name of the template used to generate the plugin configuration.
    *
    * @param context context for configuring the plugin
    * @return the name of the template used to generate the plugin configuration
    */
   String getTemplate(ConfigurationContext context);

}
