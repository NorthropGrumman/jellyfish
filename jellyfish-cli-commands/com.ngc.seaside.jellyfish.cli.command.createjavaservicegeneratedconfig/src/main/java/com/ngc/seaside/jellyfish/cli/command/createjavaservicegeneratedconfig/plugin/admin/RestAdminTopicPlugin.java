/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.admin;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationType;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.DefaultTransportTopicConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.rest.RestConfigurationDto;
import com.ngc.seaside.jellyfish.service.config.api.IAdministrationConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.dto.admin.RestAdministrationConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

/**
 * Adds configuration topics so that a service can receive and respond to administration requests.
 */
public class RestAdminTopicPlugin implements ITransportTopicConfigurationPlugin<RestConfigurationDto> {

   static final String ADMIN_TOPIC_TYPE = "com.ngc.seaside.service.admin.api.IAdministrationService";
   static final String SHUTDOWN_TOPIC_VALUE = "ADMINISTRATION_SHUTDOWN_REQUEST_TOPIC";
   static final String RESTART_TOPIC_VALUE = "ADMINISTRATION_RESTART_REQUEST_TOPIC";

   private IAdministrationConfigurationService service;

   @Inject
   public RestAdminTopicPlugin(IAdministrationConfigurationService service) {
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
               .filter(RestAdministrationConfiguration.class::isInstance)
               .map(RestAdministrationConfiguration.class::cast)
               .map(config -> getTopicConfiguration(context, config, internal))
               .flatMap(List::stream)
               .collect(Collectors.toCollection(LinkedHashSet::new));
   }

   private List<ITransportTopicConfigurationDto<RestConfigurationDto>> getTopicConfiguration(
            ConfigurationContext context, RestAdministrationConfiguration config, boolean internal) {
      RestConfigurationDto shutdownDto = new RestConfigurationDto(config.getShutdown(), !internal, internal);
      DefaultTransportTopicConfigurationDto<RestConfigurationDto> shutdownConfigDto =
               new DefaultTransportTopicConfigurationDto<>(shutdownDto);
      shutdownConfigDto.addTransportTopic(ADMIN_TOPIC_TYPE, SHUTDOWN_TOPIC_VALUE);

      RestConfigurationDto restartDto = new RestConfigurationDto(config.getRestart(), !internal, internal);
      DefaultTransportTopicConfigurationDto<RestConfigurationDto> restartConfigDto =
               new DefaultTransportTopicConfigurationDto<>(restartDto);
      restartConfigDto.addTransportTopic(ADMIN_TOPIC_TYPE, RESTART_TOPIC_VALUE);
      return Arrays.asList(shutdownConfigDto, restartConfigDto);
   }

   @Override
   public Set<String> getDependencies(ConfigurationContext context, DependencyType dependencyType) {
      return getAdminDependencies(context, dependencyType);
   }

   static Set<String> getAdminDependencies(ConfigurationContext context, DependencyType dependencyType) {
      switch (dependencyType) {
         case COMPILE:
            return Collections.singleton("com.ngc.seaside:service.admin.api");
         case BUNDLE:
            return Collections.singleton("com.ngc.seaside:service.admin.impl.defaultadminservice");
         case MODULE:
            return Collections.emptySet();
         default:
            throw new AssertionError();
      }
   }

}
