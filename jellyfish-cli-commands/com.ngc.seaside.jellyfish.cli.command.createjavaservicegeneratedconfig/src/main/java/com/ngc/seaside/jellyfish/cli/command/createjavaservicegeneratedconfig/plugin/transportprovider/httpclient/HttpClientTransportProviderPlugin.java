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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.httpclient;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.CreateJavaServiceGeneratedConfigCommand;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.ITransportProviderConfigurationPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.rest.RestConfigurationDto;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

public class HttpClientTransportProviderPlugin
         implements ITransportProviderConfigurationPlugin<HttpClientTemplateDto> {

   private Set<ITransportTopicConfigurationPlugin<RestConfigurationDto>> plugins;

   @Inject
   public HttpClientTransportProviderPlugin(Set<ITransportTopicConfigurationPlugin<RestConfigurationDto>> plugins) {
      this.plugins = plugins;
   }

   @Override
   public Optional<HttpClientTemplateDto> getConfigurationDto(ConfigurationContext context) {
      HttpClientTemplateDto dto = new HttpClientTemplateDto(context);

      for (ITransportTopicConfigurationPlugin<RestConfigurationDto> plugin : plugins) {
         plugin.getTopicConfigurations(context).forEach(dto::addTopic);
      }

      if (dto.getTopics().isEmpty()) {
         return Optional.empty();
      }

      return Optional.of(dto);
   }

   @Override
   public String getTemplate(ConfigurationContext context) {
      return CreateJavaServiceGeneratedConfigCommand.class.getPackage().getName() + "-httpclient";
   }

   @Override
   public Set<String> getDependencies(ConfigurationContext context, DependencyType dependencyType) {
      switch (dependencyType) {
         case COMPILE:
            return Collections.singleton("com.ngc.seaside:service.transport.impl.topic.httpclient");
         case BUNDLE:
            return Collections.singleton("com.ngc.seaside:service.transport.impl.provider.httpclient");
         case MODULE:
            return Collections.singleton("com.ngc.seaside:service.transport.impl.provider.httpclient.module");
         default:
            throw new AssertionError();
      }
   }

}
