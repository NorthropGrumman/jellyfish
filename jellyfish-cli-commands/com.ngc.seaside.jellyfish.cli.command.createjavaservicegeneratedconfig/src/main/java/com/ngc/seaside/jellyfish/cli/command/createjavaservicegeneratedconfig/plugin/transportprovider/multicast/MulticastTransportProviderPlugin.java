package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.multicast;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.CreateJavaServiceGeneratedConfigCommand;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.ITransportProviderConfigurationPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.multicast.MulticastConfigurationDto;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

public class MulticastTransportProviderPlugin
         implements ITransportProviderConfigurationPlugin<MulticastTemplateDto> {

   private Set<ITransportTopicConfigurationPlugin<MulticastConfigurationDto>> plugins;

   @Inject
   public MulticastTransportProviderPlugin(Set<ITransportTopicConfigurationPlugin<MulticastConfigurationDto>> plugins) {
      this.plugins = plugins;
   }

   @Override
   public Optional<MulticastTemplateDto> getConfigurationDto(ConfigurationContext context) {
      MulticastTemplateDto dto = new MulticastTemplateDto(context);

      for (ITransportTopicConfigurationPlugin<MulticastConfigurationDto> plugin : plugins) {
         plugin.getTopicConfigurations(context).forEach(dto::addTopic);
      }

      if (dto.getTopics().isEmpty()) {
         return Optional.empty();
      }

      return Optional.of(dto);
   }

   @Override
   public String getTemplate(ConfigurationContext context) {
      return CreateJavaServiceGeneratedConfigCommand.class.getPackage().getName() + "-multicast";
   }

   @Override
   public Set<String> getDependencies(ConfigurationContext context, DependencyType dependencyType) {
      switch (dependencyType) {
         case COMPILE:
            return Collections.singleton("com.ngc.seaside:service.transport.impl.topic.multicast");
         case BUNDLE:
            return Collections.singleton("com.ngc.seaside:service.transport.impl.provider.multicast");
         case MODULE:
            return Collections.singleton("com.ngc.seaside:service.transport.impl.provider.multicast.module");
         default:
            throw new AssertionError();
      }
   }

}
