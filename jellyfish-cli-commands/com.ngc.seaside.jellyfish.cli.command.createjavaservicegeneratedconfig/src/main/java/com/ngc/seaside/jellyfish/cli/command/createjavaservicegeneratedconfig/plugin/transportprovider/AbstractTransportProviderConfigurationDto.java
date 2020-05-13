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
