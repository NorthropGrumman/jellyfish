/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.telemetry;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationType;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.DefaultTransportTopicConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.rest.RestConfigurationDto;
import com.ngc.seaside.jellyfish.service.config.api.ITelemetryConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.RestTelemetryConfiguration;

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
      if (context.isSystemModel()) {
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

   @Override
   public Set<String> getDependencies(ConfigurationContext context, DependencyType dependencyType) {
      return RestTelemetryConfigurationPlugin.getRestTelemetryDependencies(context, dependencyType);
   }

}
