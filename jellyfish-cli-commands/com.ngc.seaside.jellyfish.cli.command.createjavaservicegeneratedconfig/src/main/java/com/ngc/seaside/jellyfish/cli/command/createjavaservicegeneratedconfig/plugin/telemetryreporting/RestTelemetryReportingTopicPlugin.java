package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.telemetryreporting;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationType;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.DefaultTransportTopicConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationDto.TransportTopicDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.rest.RestConfigurationDto;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.EnumDto;
import com.ngc.seaside.jellyfish.service.config.api.ITelemetryReportingConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.RestTelemetryReportingConfiguration;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

/**
 * Adds configuration topics so that a service can report its telemetry over rest.
 */
public class RestTelemetryReportingTopicPlugin implements ITransportTopicConfigurationPlugin<RestConfigurationDto> {

   private ITelemetryReportingConfigurationService service;
   private IJavaServiceGenerationService generationService;

   @Inject
   public RestTelemetryReportingTopicPlugin(ITelemetryReportingConfigurationService service,
                                            IJavaServiceGenerationService generationService) {
      this.service = service;
      this.generationService = generationService;
   }

   @Override
   public Set<ITransportTopicConfigurationDto<RestConfigurationDto>>
            getTopicConfigurations(ConfigurationContext context) {
      if (context.isSystemModel() || context.getConfigurationType() != ConfigurationType.SERVICE) {
         return Collections.emptySet();
      }
      EnumDto topicsDto = generationService.getTransportTopicsDescription(context.getOptions(), context.getModel());
      String topicsType = topicsDto.getFullyQualifiedName();
      Optional<String> topicName = service.getTransportTopicName(context.getOptions(), context.getModel());
      if (!topicName.isPresent()) {
         return Collections.emptySet();
      }
      TransportTopicDto topic = new TransportTopicDto(topicsType, topicName.get());
      return service.getConfigurations(context.getOptions(), context.getModel())
               .stream()
               .filter(RestTelemetryReportingConfiguration.class::isInstance)
               .map(RestTelemetryReportingConfiguration.class::cast)
               .map(config -> getTopicConfiguration(context, config, topic))
               .collect(Collectors.toCollection(LinkedHashSet::new));
   }

   private ITransportTopicConfigurationDto<RestConfigurationDto> getTopicConfiguration(ConfigurationContext context,
            RestTelemetryReportingConfiguration config, TransportTopicDto topic) {
      RestConfigurationDto dto = new RestConfigurationDto(config.getConfig(), true, false);
      DefaultTransportTopicConfigurationDto<RestConfigurationDto> configDto =
               new DefaultTransportTopicConfigurationDto<>(dto);
      configDto.addTransportTopic(topic);
      return configDto;
   }

   @Override
   public Set<String> getDependencies(ConfigurationContext context, DependencyType dependencyType) {
      return RestTelemetryReportingConfigurationPlugin.getTelemetryReportingDependencies(context, dependencyType);
   }

}
