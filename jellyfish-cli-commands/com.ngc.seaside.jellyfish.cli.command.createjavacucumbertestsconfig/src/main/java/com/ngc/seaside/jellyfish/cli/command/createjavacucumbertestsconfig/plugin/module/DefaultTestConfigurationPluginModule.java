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
package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertestsconfig.plugin.module;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.ngc.seaside.jellyfish.cli.command.createjavacucumbertestsconfig.plugin.TransportServiceModuleConfigurationPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.IConfigurationPlugin;

import java.util.List;

import static java.util.Arrays.asList;

public class DefaultTestConfigurationPluginModule extends AbstractModule {

   @Override
   protected void configure() {
      configurePlugins();
   }

   private void configurePlugins() {
      List<Class<? extends IConfigurationPlugin>> plugins = asList(
               TransportServiceModuleConfigurationPlugin.class);
      Multibinder<IConfigurationPlugin> binder = Multibinder.newSetBinder(binder(), IConfigurationPlugin.class);
      for (Class<? extends IConfigurationPlugin> plugin : plugins) {
         binder.addBinding().to(plugin);
      }
   }
}
