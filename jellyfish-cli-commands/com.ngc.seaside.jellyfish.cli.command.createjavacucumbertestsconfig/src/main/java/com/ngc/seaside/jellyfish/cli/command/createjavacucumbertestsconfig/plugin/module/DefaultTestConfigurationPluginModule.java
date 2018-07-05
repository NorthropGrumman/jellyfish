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
