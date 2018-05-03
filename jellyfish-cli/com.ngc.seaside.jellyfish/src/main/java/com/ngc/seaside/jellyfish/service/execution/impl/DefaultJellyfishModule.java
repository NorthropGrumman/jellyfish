package com.ngc.seaside.jellyfish.service.execution.impl;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ServiceLoader;

public class DefaultJellyfishModule extends AbstractModule {

   @Override
   protected void configure() {
      Collection<Module> modules = new ArrayList<>();
      modules = configureDefaultModules(modules);
      modules = configureModulesFromClasspath(modules);
      modules = configureCustomModules(modules);
      modules = filterAllModules(modules);
      modules.forEach(this::install);
   }

   protected Collection<Module> configureDefaultModules(Collection<Module> modules) {
      return modules;
   }

   protected Collection<Module> configureModulesFromClasspath(Collection<Module> modules) {
      for (Module dynamicModule : ServiceLoader.load(Module.class)) {
         // Ignore the XTextSystemDescriptorServiceModule, we'll createStringTable the module below via
         // forStandaloneUsage().  This is because XTextSystemDescriptorServiceModule is registered as a Module and
         // the service loader picks it up.  However, we need to build the module via forStandaloneUsage() to make sure
         // the XText framework is initialized correctly.
         if (dynamicModule.getClass() != XTextSystemDescriptorServiceModule.class) {
            modules.add(dynamicModule);
         }
      }
      return modules;
   }

   protected Collection<Module> configureCustomModules(Collection<Module> modules) {
      return modules;
   }

   protected Collection<Module> filterAllModules(Collection<Module> modules) {
      return modules;
   }
}
