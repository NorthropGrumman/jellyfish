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

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.CreateJavaServiceGeneratedConfigCommand;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationType;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.IConfigurationTemplatePlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.readiness.IReadinessPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.readiness.ReadinessTemplateDto;
import com.ngc.seaside.jellyfish.service.config.api.ITelemetryConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.RestTelemetryConfiguration;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

/**
 * Configuration plugin for the telemetry component of a project.
 */
public class RestTelemetryConfigurationPlugin
         implements IConfigurationTemplatePlugin<RestTelemetryTemplateDto>, IReadinessPlugin {

   private ITelemetryConfigurationService service;

   @Inject
   public RestTelemetryConfigurationPlugin(ITelemetryConfigurationService service) {
      this.service = service;
   }

   @Override
   public Optional<RestTelemetryTemplateDto> getConfigurationDto(ConfigurationContext context) {
      if (context.isSystemModel() || context.getConfigurationType() != ConfigurationType.SERVICE) {
         return Optional.empty();
      }
      boolean hasTelemetry = service.getConfigurations(context.getOptions(), context.getModel())
               .stream()
               .anyMatch(RestTelemetryConfiguration.class::isInstance);
      RestTelemetryTemplateDto dto = null;
      if (hasTelemetry) {
         dto = new RestTelemetryTemplateDto(context, context.getModel().getName() + "TelemetryConfiguration");
      }
      return Optional.ofNullable(dto);
   }

   @Override
   public String getTemplate(ConfigurationContext context) {
      return CreateJavaServiceGeneratedConfigCommand.class.getPackage().getName() + "-telemetry";
   }

   @Override
   public Set<String> getDependencies(ConfigurationContext context, DependencyType dependencyType) {
      return getRestTelemetryDependencies(context, dependencyType);
   }

   static Set<String> getRestTelemetryDependencies(ConfigurationContext context, DependencyType dependencyType) {
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
      Optional<RestTelemetryTemplateDto> telemetryDto = getConfigurationDto(dto.getContext());
      if (telemetryDto.isPresent()) {
         String telemetryConfiguration = telemetryDto.get().getFullyQualifiedName();
         dto.addClass(RestTelemetryTopicPlugin.TELEMETRY_TOPIC_TYPE);
         dto.addClass(telemetryConfiguration);
      }
   }
}
