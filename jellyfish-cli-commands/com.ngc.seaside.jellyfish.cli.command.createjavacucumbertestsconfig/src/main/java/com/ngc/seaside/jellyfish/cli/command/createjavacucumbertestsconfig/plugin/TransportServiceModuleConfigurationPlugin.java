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
package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertestsconfig.plugin;

import com.ngc.seaside.jellyfish.cli.command.createjavacucumbertestsconfig.CreateJavaCucumberTestsConfigCommand;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationType;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.IConfigurationTemplatePlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.ITransportProviderConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.ITransportProviderConfigurationPlugin;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

public class TransportServiceModuleConfigurationPlugin
         implements IConfigurationTemplatePlugin<TransportServiceModuleTemplateDto> {

   private Set<ITransportProviderConfigurationPlugin<? extends ITransportProviderConfigurationDto>> plugins;

   @Inject
   public TransportServiceModuleConfigurationPlugin(Set<
            ITransportProviderConfigurationPlugin<? extends ITransportProviderConfigurationDto>> plugins) {
      this.plugins = plugins;
   }

   @Override
   public Optional<TransportServiceModuleTemplateDto> getConfigurationDto(ConfigurationContext context) {
      if (context.getConfigurationType() != ConfigurationType.TEST) {
         return Optional.empty();
      }
      
      TransportServiceModuleTemplateDto dto = new TransportServiceModuleTemplateDto(context);

      plugins.stream()
               .map(plugin -> plugin.getConfigurationDto(context))
               .filter(Optional::isPresent)
               .map(Optional::get)
               .map(ITransportProviderConfigurationDto::getModuleType)
               .forEach(dto::addModule);

      return Optional.of(dto);
   }

   @Override
   public String getTemplate(ConfigurationContext context) {
      return CreateJavaCucumberTestsConfigCommand.class.getPackage().getName() + "-transportservicemodule";
   }

   @Override
   public Set<String> getDependencies(ConfigurationContext context, DependencyType dependencyType) {
      switch (dependencyType) {
         case COMPILE:
            return Collections.singleton("com.google.inject:guice");
         case BUNDLE:
         case MODULE:
            return Collections.emptySet();
         default:
            throw new AssertionError();
      }
   }
}
