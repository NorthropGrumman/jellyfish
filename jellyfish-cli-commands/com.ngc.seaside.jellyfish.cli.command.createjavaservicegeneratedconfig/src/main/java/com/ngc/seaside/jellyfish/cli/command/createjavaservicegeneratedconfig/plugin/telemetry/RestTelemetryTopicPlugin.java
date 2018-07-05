package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.telemetry;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationType;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.DefaultTransportTopicConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.rest.RestConfigurationDto;
import com.ngc.seaside.jellyfish.service.config.api.ITelemetryConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.RestTelemetryConfiguration;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

/**
 * Adds configuration topics so that a service can receive and respond to telemetry requests.
 */
public class RestTelemetryTopicPlugin implements ITransportTopicConfigurationPlugin<RestConfigurationDto> {

   static final String TELEMETRY_TOPIC_TYPE = "com.ngc.seaside.service.telemetry.api.ITelemetryService";
   private static final String TELEMETRY_TOPIC_VALUE = "TELEMETRY_REQUEST_TRANSPORT_TOPIC";

   private ITelemetryConfigurationService service;

   @Inject
   public RestTelemetryTopicPlugin(ITelemetryConfigurationService service) {
      this.service = service;
   }

   @Override
   public Set<ITransportTopicConfigurationDto<RestConfigurationDto>>
            getTopicConfigurations(ConfigurationContext context) {
      if (isSystemModel(context.getModel())) {
         return Collections.emptySet();
      }

      ConfigurationType configurationType = context.getConfigurationType();
      if (configurationType != ConfigurationType.SERVICE && configurationType != ConfigurationType.TEST) {
         return Collections.emptySet();
      }
      boolean internal = configurationType == ConfigurationType.SERVICE;

      return service.getConfigurations(context.getOptions(), context.getModel())
               .stream()
               .filter(RestTelemetryConfiguration.class::isInstance)
               .map(RestTelemetryConfiguration.class::cast)
               .map(config -> getTopicConfiguration(context, config, internal))
               .collect(Collectors.toCollection(LinkedHashSet::new));
   }

   private ITransportTopicConfigurationDto<RestConfigurationDto> getTopicConfiguration(ConfigurationContext context,
            RestTelemetryConfiguration config, boolean internal) {

      RestConfigurationDto dto = new RestConfigurationDto(config.getConfig(), !internal, internal);
      DefaultTransportTopicConfigurationDto<RestConfigurationDto> configDto =
               new DefaultTransportTopicConfigurationDto<>(dto);
      configDto.addTransportTopic(TELEMETRY_TOPIC_TYPE, TELEMETRY_TOPIC_VALUE);
      return configDto;
   }

   static boolean isSystemModel(IModel model) {
      return !model.getParts().isEmpty();
   }

   @Override
   public Set<String> getDependencies(ConfigurationContext context, DependencyType dependencyType) {
      return RestTelemetryConfigurationPlugin.getRestTelemetryDependencies(context, dependencyType);
   }

}
