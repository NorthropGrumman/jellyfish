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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.telemetryreporting;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

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
import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;

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
      if (context.isSystemModel() || context.getConfigurationType() != ConfigurationType.SERVICE) {
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
