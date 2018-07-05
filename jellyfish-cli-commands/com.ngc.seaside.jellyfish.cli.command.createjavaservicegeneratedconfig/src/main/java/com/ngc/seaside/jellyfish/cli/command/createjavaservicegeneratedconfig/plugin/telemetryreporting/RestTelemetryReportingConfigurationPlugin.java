package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.telemetryreporting;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.CreateJavaServiceGeneratedConfigCommand;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationType;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.IConfigurationTemplatePlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.readiness.IReadinessPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.readiness.ReadinessTemplateDto;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.EnumDto;
import com.ngc.seaside.jellyfish.service.config.api.ITelemetryReportingConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.RestTelemetryReportingConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.TelemetryReportingConfiguration;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

/**
 * Configuration plugin for the telemetry reporting component of the configuration project.
 */
public class RestTelemetryReportingConfigurationPlugin
         implements IConfigurationTemplatePlugin<RestTelemetryReportingTemplateDto>, IReadinessPlugin {

   private ITelemetryReportingConfigurationService service;
   private ILogService logService;
   private IJavaServiceGenerationService generateService;

   @Inject
   public RestTelemetryReportingConfigurationPlugin(ILogService logService,
                                                    ITelemetryReportingConfigurationService service,
                                                    IJavaServiceGenerationService generateService) {
      this.logService = logService;
      this.service = service;
      this.generateService = generateService;
   }

   @Override
   public Optional<RestTelemetryReportingTemplateDto> getConfigurationDto(ConfigurationContext context) {
      if (RestTelemetryReportingTopicPlugin.isSystemModel(context.getModel())
               || context.getConfigurationType() != ConfigurationType.SERVICE) {
         return Optional.empty();
      }
      Set<RestTelemetryReportingConfiguration> configs =
               service.getConfigurations(context.getOptions(), context.getModel())
                        .stream()
                        .filter(RestTelemetryReportingConfiguration.class::isInstance)
                        .map(RestTelemetryReportingConfiguration.class::cast)
                        .collect(Collectors.toCollection(LinkedHashSet::new));

      if (configs.isEmpty()) {
         return Optional.empty();
      }

      long rates = configs.stream().mapToInt(TelemetryReportingConfiguration::getRateInMilliseconds).distinct()
               .count();

      if (rates > 1) {
         logService.warn(getClass(), "Cannot generate multiple telemetry reporting with different rates");
         return Optional.empty();
      }

      Optional<String> topicValue = service.getTransportTopicName(context.getOptions(), context.getModel());
      if (!topicValue.isPresent()) {
         return Optional.empty();
      }
      EnumDto topicsEnum = generateService.getTransportTopicsDescription(context.getOptions(), context.getModel());
      String topicType = topicsEnum.getFullyQualifiedName();

      RestTelemetryReportingTemplateDto dto =
               new RestTelemetryReportingTemplateDto(context,
                        context.getModel().getName() + "TelemetryReporting", topicType, topicValue.get(),
                        configs.iterator().next().getRateInMilliseconds());

      return Optional.ofNullable(dto);
   }

   @Override
   public String getTemplate(ConfigurationContext context) {
      return CreateJavaServiceGeneratedConfigCommand.class.getPackage().getName() + "-telemetryreporting";
   }

   @Override
   public Set<String> getDependencies(ConfigurationContext context, DependencyType dependencyType) {
      return getTelemetryReportingDependencies(context, dependencyType);
   }

   static Set<String> getTelemetryReportingDependencies(ConfigurationContext context, DependencyType dependencyType) {
      switch (dependencyType) {
         case COMPILE:
            return Collections.singleton("com.ngc.seaside:service.telemetry.api");
         case BUNDLE:
            return Collections.singleton("com.ngc.seaside:service.telemetry.impl.jsontelemetryservice");
         case MODULE:
            return Collections.singleton("com.ngc.seaside:service.telemetry.impl.jsontelemetryservice.module");
         default:
            throw new AssertionError();
      }
   }

   @Override
   public void configure(ReadinessTemplateDto dto) {
      Optional<RestTelemetryReportingTemplateDto> telemetryDto = getConfigurationDto(dto.getContext());
      if (telemetryDto.isPresent()) {
         String telemetryConfiguration = telemetryDto.get().getFullyQualifiedName();
         dto.addClass(telemetryConfiguration);
      }
   }

}
