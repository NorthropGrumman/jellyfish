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
      if (!context.isSystemModel() || context.getConfigurationType() != ConfigurationType.TEST) {
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
               RestAdminTopicPlugin.RESTART_TOPIC_VALUE);
      return Arrays.asList(shutdownConfigDto, restartConfigDto);
   }

   @Override
   public Set<String> getDependencies(ConfigurationContext context, DependencyType dependencyType) {
      return RestAdminTopicPlugin.getAdminDependencies(context, dependencyType);
   }

}
