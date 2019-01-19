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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportservice;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.CreateJavaServiceGeneratedConfigCommand;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationType;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.IConfigurationTemplatePlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.ITransportProviderConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.ITransportProviderConfigurationPlugin;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

/**
 * Plugin for configuring the transport service. This plugin expects {@link ITransportProviderConfigurationPlugin}s
 * and delegates most of its configuration to those plugins.
 */
public class TransportServiceConfigurationPlugin implements IConfigurationTemplatePlugin<TransportServiceTemplateDto> {

   private static final Set<String> COMPILE_DEPENDENCIES =
            setOf("com.ngc.seaside:service.transport.api", "com.ngc.blocs:service.api");
   private static final Set<String> BUNDLE_DEPENDENCIES =
            setOf("com.ngc.seaside:service.transport.impl.defaulttransportservice");
   private static final Set<String> MODULE_DEPENDENCIES =
            setOf("com.ngc.seaside:service.transport.impl.defaulttransportservice.module");

   private Set<ITransportProviderConfigurationPlugin<? extends ITransportProviderConfigurationDto>> plugins;

   @Inject
   public TransportServiceConfigurationPlugin(Set<
            ITransportProviderConfigurationPlugin<? extends ITransportProviderConfigurationDto>> plugins) {
      this.plugins = plugins;
   }

   @Override
   public Optional<TransportServiceTemplateDto> getConfigurationDto(ConfigurationContext context) {
      ConfigurationType configurationType = context.getConfigurationType();
      if (configurationType != ConfigurationType.SERVICE && configurationType != ConfigurationType.TEST) {
         return Optional.empty();
      }

      TransportServiceTemplateDto dto = new TransportServiceTemplateDto(context);
      dto.setClassName(context.getModel().getName() + (configurationType == ConfigurationType.TEST ? "Test" : "")
               + "TransportConfiguration");

      this.plugins.stream()
               .map(plugin -> plugin.getConfigurationDto(context))
               .filter(Optional::isPresent)
               .map(Optional::get)
               .forEach(dto::addTransportProvider);

      return Optional.of(dto);
   }

   @Override
   public String getTemplate(ConfigurationContext context) {
      if (context.getConfigurationType() == ConfigurationType.SERVICE) {
         return CreateJavaServiceGeneratedConfigCommand.class.getPackage().getName() + "-transportservice";
      } else {
         return CreateJavaServiceGeneratedConfigCommand.class.getPackage().getName() + "-transportserviceguice";
      }
   }

   @Override
   public Set<String> getDependencies(ConfigurationContext context, DependencyType dependencyType) {
      switch (dependencyType) {
         case COMPILE:
            return COMPILE_DEPENDENCIES;
         case BUNDLE:
            return BUNDLE_DEPENDENCIES;
         case MODULE:
            return MODULE_DEPENDENCIES;
         default:
            throw new AssertionError();
      }
   }

   @Override
   public Map<String, Object> getExtraTemplateParameters(ConfigurationContext context) {
      return Collections.singletonMap(StringUtils.class.getSimpleName(), StringUtils.class);
   }

   private static final Set<String> setOf(String... values) {
      return Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(values)));
   }

}
