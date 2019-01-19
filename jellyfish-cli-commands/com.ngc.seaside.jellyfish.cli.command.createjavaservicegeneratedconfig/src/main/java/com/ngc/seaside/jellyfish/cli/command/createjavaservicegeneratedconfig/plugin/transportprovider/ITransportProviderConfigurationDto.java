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
