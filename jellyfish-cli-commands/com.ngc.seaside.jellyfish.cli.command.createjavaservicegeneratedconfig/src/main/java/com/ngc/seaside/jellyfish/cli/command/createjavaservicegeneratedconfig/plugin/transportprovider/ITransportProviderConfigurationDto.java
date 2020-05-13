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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider;

import org.apache.commons.lang3.StringUtils;

/**
 * Interface for a transport provider dto.
 */
public interface ITransportProviderConfigurationDto {

   /**
    * Returns the fully-qualified name of the transport provider's topic type.
    * 
    * @return the fully-qualified name of the transport provider's topic type
    */
   String getTopicType();

   /**
    * Returns the simple class name of the {@link #getTopicType() topic type}.
    * 
    * @return the simple class name of the {@link #getTopicType() topic type}
    */
   default String getTopicClassName() {
      String topicType = getTopicType();
      return topicType.substring(topicType.lastIndexOf('.') + 1);
   }

   /**
    * Returns the fully-qualified name of the class used to configure the transport provider. This class should
    * have a method with the signature
    * {@code public static void configure(TransportConfiguration, ITransportProvider<T>)}.
    * 
    * @return the fully-qualified name of the class used to configure the transport provider.
    */
   String getConfigurationType();

   /**
    * Returns the simple class name of the {@link #getConfigurationType() configuration type}.
    * 
    * @return the simple class name of the {@link #getConfigurationType() configuration type}
    */
   default String getConfigurationClassName() {
      String configurationType = getConfigurationType();
      return configurationType.substring(configurationType.lastIndexOf('.') + 1);
   }

   /**
    * Returns the target filter for referencing the transport provider in OSGi. Typically, this will be
    * {@code "(component.name=<fully-qualified-name>)"}.
    * 
    * @return the target filter for referencing the transport provider in OSGi
    */
   String getProviderTarget();

   /**
    * Returns the fully-qualified name of the transport provider's Guice module.
    * 
    * @return the fully-qualified name of the transport provider's Guice module
    */
   String getModuleType();

   /**
    * Returns a unique variable name that can be used for this transport provider.
    * 
    * @return a unique variable name that can be used for this transport provider
    */
   default String getProviderVariableName() {
      String topicName = getTopicClassName();
      topicName = StringUtils.uncapitalize(topicName);
      if (topicName.endsWith("Topic")) {
         return topicName.substring(0, topicName.length() - "Topic".length()) + "Provider";
      }
      return topicName + "Provider";
   }

}
