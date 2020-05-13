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
