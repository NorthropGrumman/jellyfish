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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.BaseConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;

public abstract class AbstractTransportProviderConfigurationDto extends BaseConfigurationDto
         implements ITransportProviderConfigurationDto {

   private final String topicType;
   private final String configurationType;
   private final String providerTarget;
   private final String moduleType;

   /**
    * Constructor for a transport provider configuration dto.
    * 
    * @param context context
    * @param topicType fully-qualified name of the topic class
    * @param configurationTypeSuffix the suffix for the generated configuration class for the provider
    * @param providerTarget the OSGi target filter for referencing the provider
    * @param moduleType the fully-qualified name of the Guice module for injecting the provider
    */
   public AbstractTransportProviderConfigurationDto(ConfigurationContext context, String topicType,
                                                    String configurationTypeSuffix, String providerTarget,
                                                    String moduleType) {
      super(context);
      this.topicType = topicType;
      this.configurationType = context.getBasePackage() + "." + context.getModel().getName() + configurationTypeSuffix;
      this.providerTarget = providerTarget;
      this.moduleType = moduleType;
   }

   @Override
   public String getTopicType() {
      return topicType;
   }

   @Override
   public String getConfigurationType() {
      return configurationType;
   }

   @Override
   public String getProviderTarget() {
      return providerTarget;
   }

   @Override
   public String getModuleType() {
      return moduleType;
   }

}
