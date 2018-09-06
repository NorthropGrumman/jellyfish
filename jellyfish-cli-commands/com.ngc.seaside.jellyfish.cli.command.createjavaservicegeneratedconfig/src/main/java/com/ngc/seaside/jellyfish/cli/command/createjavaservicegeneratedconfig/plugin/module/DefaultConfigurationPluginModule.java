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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.module;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.IConfigurationPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.admin.RestAdminSystemTopicPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.admin.RestAdminTopicPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.io.MulticastIOTopicPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.io.RestIOTopicPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.io.ZeroMqIOTopicPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.readiness.ConnectorReadinessPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.readiness.IReadinessPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.readiness.PubSubBridgeReadinessPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.readiness.ReadinessConfigurationPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.telemetry.RestTelemetryConfigurationPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.telemetry.RestTelemetrySystemTopicPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.telemetry.RestTelemetryTopicPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.telemetryreporting.RestTelemetryReportingConfigurationPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.telemetryreporting.RestTelemetryReportingTopicPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.ITransportProviderConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.ITransportProviderConfigurationPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.httpclient.HttpClientTransportProviderPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.multicast.MulticastTransportProviderPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.spark.SparkTransportProviderPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.zeromq.ZeroMqTcpTransportProviderPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportservice.TransportServiceConfigurationPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.ITransportTopicConfigurationPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.multicast.MulticastConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.rest.RestConfigurationDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.zeromq.ZeroMqTcpConfigurationDto;

import java.util.List;

import static java.util.Arrays.asList;

public class DefaultConfigurationPluginModule extends AbstractModule {

   @Override
   protected void configure() {
      configurePlugins();
      configureTransportProviderPlugins();
      configureTransportTopicPlugins();
      configureReadinessPlugins();
   }

   private void configurePlugins() {
      List<Class<? extends IConfigurationPlugin>> plugins = asList(
               RestAdminTopicPlugin.class,
               RestAdminSystemTopicPlugin.class,
               MulticastIOTopicPlugin.class,
               RestIOTopicPlugin.class,
               ZeroMqIOTopicPlugin.class,
               ReadinessConfigurationPlugin.class,
               RestTelemetryConfigurationPlugin.class,
               RestTelemetrySystemTopicPlugin.class,
               RestTelemetryTopicPlugin.class,
               RestTelemetryReportingConfigurationPlugin.class,
               RestTelemetryReportingTopicPlugin.class,
               HttpClientTransportProviderPlugin.class,
               MulticastTransportProviderPlugin.class,
               SparkTransportProviderPlugin.class,
               ZeroMqTcpTransportProviderPlugin.class,
               TransportServiceConfigurationPlugin.class);
      Multibinder<IConfigurationPlugin> binder = Multibinder.newSetBinder(binder(), IConfigurationPlugin.class);
      for (Class<? extends IConfigurationPlugin> plugin : plugins) {
         binder.addBinding().to(plugin);
      }
   }

   private void configureTransportProviderPlugins() {
      List<Class<
               ? extends ITransportProviderConfigurationPlugin<? extends ITransportProviderConfigurationDto>>> plugins =
                        asList(
                                 HttpClientTransportProviderPlugin.class,
                                 MulticastTransportProviderPlugin.class,
                                 SparkTransportProviderPlugin.class,
                                 ZeroMqTcpTransportProviderPlugin.class);
      Multibinder<ITransportProviderConfigurationPlugin<? extends ITransportProviderConfigurationDto>> binder =
               Multibinder.newSetBinder(binder(), new TypeLiteral<
                        ITransportProviderConfigurationPlugin<? extends ITransportProviderConfigurationDto>>() {
               });
      for (Class<? extends ITransportProviderConfigurationPlugin<
               ? extends ITransportProviderConfigurationDto>> plugin : plugins) {
         binder.addBinding().to(plugin);
      }
   }

   private void configureTransportTopicPlugins() {
      List<Class<? extends ITransportTopicConfigurationPlugin<MulticastConfigurationDto>>> multicast = asList(
               MulticastIOTopicPlugin.class);
      List<Class<? extends ITransportTopicConfigurationPlugin<ZeroMqTcpConfigurationDto>>> zeromqTcp = asList(
               ZeroMqIOTopicPlugin.class);
      List<Class<? extends ITransportTopicConfigurationPlugin<RestConfigurationDto>>> rest = asList(
               RestAdminTopicPlugin.class,
               RestAdminSystemTopicPlugin.class,
               RestIOTopicPlugin.class,
               RestTelemetrySystemTopicPlugin.class,
               RestTelemetryTopicPlugin.class,
               RestTelemetryReportingTopicPlugin.class);

      configureTransportTopicPlugins(new TypeLiteral<ITransportTopicConfigurationPlugin<MulticastConfigurationDto>>() {
      }, multicast);
      configureTransportTopicPlugins(new TypeLiteral<ITransportTopicConfigurationPlugin<RestConfigurationDto>>() {
      }, rest);
      configureTransportTopicPlugins(new TypeLiteral<ITransportTopicConfigurationPlugin<ZeroMqTcpConfigurationDto>>() {
      }, zeromqTcp);

   }

   private <T, U extends ITransportTopicConfigurationPlugin<T>> void
            configureTransportTopicPlugins(TypeLiteral<U> literal, List<Class<? extends U>> plugins) {
      Multibinder<U> binder = Multibinder.newSetBinder(binder(), literal);

      for (Class<? extends U> plugin : plugins) {
         binder.addBinding().to(plugin);
      }
   }

   private void configureReadinessPlugins() {
      List<Class<? extends IReadinessPlugin>> plugins = asList(
               ConnectorReadinessPlugin.class, PubSubBridgeReadinessPlugin.class,
               RestTelemetryConfigurationPlugin.class, RestTelemetryReportingConfigurationPlugin.class);

      Multibinder<IReadinessPlugin> binder = Multibinder.newSetBinder(binder(), IReadinessPlugin.class);
      for (Class<? extends IReadinessPlugin> plugin : plugins) {
         binder.addBinding().to(plugin);
      }
   }

}
