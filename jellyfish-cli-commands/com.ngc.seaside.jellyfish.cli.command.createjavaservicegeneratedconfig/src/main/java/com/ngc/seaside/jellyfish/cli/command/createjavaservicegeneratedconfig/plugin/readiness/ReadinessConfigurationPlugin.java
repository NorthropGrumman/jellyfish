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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.readiness;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.CreateJavaServiceGeneratedConfigCommand;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationType;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.IConfigurationTemplatePlugin;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

public class ReadinessConfigurationPlugin implements IConfigurationTemplatePlugin<ReadinessTemplateDto> {

   private Set<IReadinessPlugin> plugins;

   @Inject
   public ReadinessConfigurationPlugin(Set<IReadinessPlugin> plugins) {
      this.plugins = plugins;
   }

   @Override
   public Optional<ReadinessTemplateDto> getConfigurationDto(ConfigurationContext context) {
      if (context.getConfigurationType() != ConfigurationType.SERVICE) {
         return Optional.empty();
      }

      ReadinessTemplateDto dto = new ReadinessTemplateDto(context);

      for (IReadinessPlugin plugin : plugins) {
         plugin.configure(dto);
      }

      if (dto.getComponents().isEmpty() && dto.getEventSubscribers().isEmpty() && dto.getClasses().isEmpty()) {
         return Optional.empty();
      }

      return Optional.of(dto);
   }

   @Override
   public String getTemplate(ConfigurationContext context) {
      return CreateJavaServiceGeneratedConfigCommand.class.getPackage().getName() + "-readiness";
   }

   @Override
   public Set<String> getDependencies(ConfigurationContext context, DependencyType dependencyType) {
      switch (dependencyType) {
         case COMPILE:
            return Collections.singleton("com.ngc.seaside:service.api");
         case BUNDLE:
            return Collections.singleton("com.ngc.seaside:service.readiness.impl.defaultreadinessservice");
         case MODULE:
            return Collections.singleton("com.ngc.seaside:service.readiness.impl.defaultreadinessservice.module");
         default:
            throw new AssertionError();
      }
   }
}
