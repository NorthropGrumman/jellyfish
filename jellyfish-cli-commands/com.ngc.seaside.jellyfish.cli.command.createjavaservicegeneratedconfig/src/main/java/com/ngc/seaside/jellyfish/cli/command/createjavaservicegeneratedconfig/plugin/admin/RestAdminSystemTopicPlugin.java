package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.admin;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationType;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.DefaultTransportTopicConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.rest.RestConfigurationDto;
import com.ngc.seaside.jellyfish.service.config.api.IAdministrationConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.dto.admin.RestAdministrationConfiguration;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

/**
 * Adds configuration topics so that a system model project can send remote admin requests to all of its sub-service
 * parts.
 */
public class RestAdminSystemTopicPlugin implements ITransportTopicConfigurationPlugin<RestConfigurationDto> {

   private IAdministrationConfigurationService service;

   @Inject
   public RestAdminSystemTopicPlugin(IAdministrationConfigurationService service) {
      this.service = service;
   }

   @Override
   public Set<ITransportTopicConfigurationDto<RestConfigurationDto>>
            getTopicConfigurations(ConfigurationContext context) {
      IModel model = context.getModel();
      if (!RestAdminTopicPlugin.isSystemModel(model) || context.getConfigurationType() != ConfigurationType.TEST) {
         return Collections.emptySet();
      }
      Set<ITransportTopicConfigurationDto<RestConfigurationDto>> set = new LinkedHashSet<>();
      for (IModelReferenceField part : model.getParts()) {
         IModel modelPart = part.getType();
         Set<ITransportTopicConfigurationDto<RestConfigurationDto>> partConfig =
                  getAdminConfigurations(context, modelPart);
         set.addAll(partConfig);
      }

      return set;
   }

   private Set<ITransportTopicConfigurationDto<RestConfigurationDto>> getAdminConfigurations(
            ConfigurationContext context, IModel part) {
      return service.getConfigurations(context.getOptions(), part).stream()
               .filter(RestAdministrationConfiguration.class::isInstance)
               .map(RestAdministrationConfiguration.class::cast)
               .map(config -> getTopicConfiguration(context, config))
               .flatMap(List::stream)
               .collect(Collectors.toCollection(LinkedHashSet::new));
   }

   private List<ITransportTopicConfigurationDto<RestConfigurationDto>> getTopicConfiguration(
            ConfigurationContext context, RestAdministrationConfiguration config) {
      RestConfigurationDto shutdownDto = new RestConfigurationDto(config.getShutdown(), true, false);
      DefaultTransportTopicConfigurationDto<RestConfigurationDto> shutdownConfigDto =
               new DefaultTransportTopicConfigurationDto<>(shutdownDto);
      shutdownConfigDto.addTransportTopic(RestAdminTopicPlugin.ADMIN_TOPIC_TYPE,
               RestAdminTopicPlugin.SHUTDOWN_TOPIC_VALUE);

      RestConfigurationDto restartDto = new RestConfigurationDto(config.getRestart(), true, false);
      DefaultTransportTopicConfigurationDto<RestConfigurationDto> restartConfigDto =
               new DefaultTransportTopicConfigurationDto<>(restartDto);
      restartConfigDto.addTransportTopic(RestAdminTopicPlugin.ADMIN_TOPIC_TYPE,
               RestAdminTopicPlugin.SHUTDOWN_TOPIC_VALUE);
      return Arrays.asList(shutdownConfigDto, restartConfigDto);
   }

   @Override
   public Set<String> getDependencies(ConfigurationContext context, DependencyType dependencyType) {
      return RestAdminTopicPlugin.getAdminDependencies(context, dependencyType);
   }

}
