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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.telemetry;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationType;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.DefaultTransportTopicConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationDto.TransportTopicDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.rest.RestConfigurationDto;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.config.api.ITelemetryConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.RestTelemetryConfiguration;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

/**
 * Adds configuration topics so that a system model project can send remote telemetry requests to its sub-service parts.
 */
public class RestTelemetrySystemTopicPlugin implements ITransportTopicConfigurationPlugin<RestConfigurationDto> {

   private ITelemetryConfigurationService service;
   private IJavaServiceGenerationService generationService;

   @Inject
   public RestTelemetrySystemTopicPlugin(ITelemetryConfigurationService service,
                                         IJavaServiceGenerationService generationService) {
      this.service = service;
      this.generationService = generationService;
   }

   @Override
   public Set<ITransportTopicConfigurationDto<RestConfigurationDto>>
            getTopicConfigurations(ConfigurationContext context) {
      IModel model = context.getModel();
      if (!context.isSystemModel() || context.getConfigurationType() != ConfigurationType.TEST) {
         return Collections.emptySet();
      }
      String topicsType =
               generationService.getTransportTopicsDescription(context.getOptions(), model).getFullyQualifiedName();
      Set<ITransportTopicConfigurationDto<RestConfigurationDto>> set = new LinkedHashSet<>();
      for (IModelReferenceField part : model.getParts()) {
         Optional<String> telemetryTopic = service.getTransportTopicName(context.getOptions(), part);
         if (!telemetryTopic.isPresent()) {
            return Collections.emptySet();
         }
         IModel modelPart = part.getType();
         TransportTopicDto topic = new TransportTopicDto(topicsType, telemetryTopic.get());
         Set<ITransportTopicConfigurationDto<RestConfigurationDto>> partConfig =
                  getPartTelemetryConfigurations(context, modelPart, topic);
         set.addAll(partConfig);
      }

      return set;
   }

   private Set<ITransportTopicConfigurationDto<RestConfigurationDto>>
            getPartTelemetryConfigurations(ConfigurationContext context, IModel part, TransportTopicDto topic) {
      return service.getConfigurations(context.getOptions(), part).stream()
               .filter(RestTelemetryConfiguration.class::isInstance)
               .map(RestTelemetryConfiguration.class::cast)
               .map(config -> getTopicConfiguration(context, config, topic))
               .collect(Collectors.toCollection(LinkedHashSet::new));
   }

   private ITransportTopicConfigurationDto<RestConfigurationDto> getTopicConfiguration(ConfigurationContext context,
            RestTelemetryConfiguration config, TransportTopicDto topic) {
      RestConfigurationDto dto = new RestConfigurationDto(config.getConfig(), true, false);
      DefaultTransportTopicConfigurationDto<RestConfigurationDto> configDto =
               new DefaultTransportTopicConfigurationDto<>(dto);
      configDto.addTransportTopic(topic);
      return configDto;
   }

   @Override
   public Set<String> getDependencies(ConfigurationContext context, DependencyType dependencyType) {
      return RestTelemetryConfigurationPlugin.getRestTelemetryDependencies(context, dependencyType);
   }

}
