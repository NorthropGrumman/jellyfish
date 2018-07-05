package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.zeromq;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.CreateJavaServiceGeneratedConfigCommand;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.ITransportProviderConfigurationPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.zeromq.ZeroMqTcpConfigurationDto;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

public class ZeroMqTcpTransportProviderPlugin
         implements ITransportProviderConfigurationPlugin<ZeroMqTcpTemplateDto> {

   private Set<ITransportTopicConfigurationPlugin<ZeroMqTcpConfigurationDto>> plugins;

   @Inject
   public ZeroMqTcpTransportProviderPlugin(Set<ITransportTopicConfigurationPlugin<ZeroMqTcpConfigurationDto>> plugins) {
      this.plugins = plugins;
   }

   @Override
   public Optional<ZeroMqTcpTemplateDto> getConfigurationDto(ConfigurationContext context) {
      ZeroMqTcpTemplateDto dto = new ZeroMqTcpTemplateDto(context);

      for (ITransportTopicConfigurationPlugin<ZeroMqTcpConfigurationDto> plugin : plugins) {
         plugin.getTopicConfigurations(context).forEach(dto::addTopic);
      }

      if (dto.getTopics().isEmpty()) {
         return Optional.empty();
      }

      return Optional.of(dto);
   }

   @Override
   public String getTemplate(ConfigurationContext context) {
      return CreateJavaServiceGeneratedConfigCommand.class.getPackage().getName() + "-zeromq";
   }

   @Override
   public Set<String> getDependencies(ConfigurationContext context, DependencyType dependencyType) {
      switch (dependencyType) {
         case COMPILE:
            return Collections.singleton("com.ngc.seaside:service.transport.impl.topic.zeromq");
         case BUNDLE:
            return Collections.singleton("com.ngc.seaside:service.transport.impl.provider.zeromq");
         case MODULE:
            return Collections.singleton("com.ngc.seaside:service.transport.impl.provider.zeromq.module");
         default:
            throw new AssertionError();
      }
   }

}
