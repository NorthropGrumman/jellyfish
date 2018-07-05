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
