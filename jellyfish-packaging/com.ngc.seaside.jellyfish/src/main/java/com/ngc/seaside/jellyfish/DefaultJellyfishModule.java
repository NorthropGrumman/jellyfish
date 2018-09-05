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
package com.ngc.seaside.jellyfish;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

import com.ngc.blocs.guice.module.LogServiceModule;
import com.ngc.blocs.guice.module.ResourceServiceModule;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ServiceLoader;

/**
 * The default Jellyfish Guice module.  Clients can use this module directly when {@link
 * com.ngc.seaside.jellyfish.service.execution.api.IJellyfishService#run(String, Collection, Collection) running
 * Jellyfish} or they can extend this class to allow for customization of the modules.  For example, extending classes
 * can implement {@code configureCustomModules} to register additional modules.  {@code filterAllModules} can be
 * overridden to filter out any unwanted default modules.
 */
public class DefaultJellyfishModule extends AbstractModule {

   @Override
   protected void configure() {
      Collection<Module> modules = new ArrayList<>();
      modules = configureDefaultModules(modules);
      modules = configureModulesFromClasspath(modules);
      modules = configureCustomModules(modules);
      modules = filterAllModules(modules);
      // Install the file collection of modules.
      modules.forEach(this::install);
   }

   /**
    * Invoked to configure the default modules.  It most cases, simply add default modules to the passed in collection
    * and return it.
    *
    * @param modules the modules configured thus far
    * @return the modules configured this far
    */
   protected Collection<Module> configureDefaultModules(Collection<Module> modules) {
      // Register the standalone version of the XText service.
      modules.add(XTextSystemDescriptorServiceModule.forStandaloneUsage());
      modules.add(new LogServiceModule());
      modules.add(new ResourceServiceModule());
      return modules;
   }

   /**
    * Invoked to add any modules from the classpath via {@link ServiceLoader}s.  The default implementation uses {@code
    * ServiceLoader.load(Module.class)} to find modules on the classpath.
    *
    * @param modules the modules configured thus far
    * @return the modules configured this far
    */
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

   /**
    * Invoked to configure additional modules.  It most cases, simply add modules to the passed in collection
    * and return it.  The default implementation does nothing.
    *
    * @param modules the modules configured thus far
    * @return the modules configured this far
    */
   protected Collection<Module> configureCustomModules(Collection<Module> modules) {
      return modules;
   }

   /**
    * Invoked to filter all modules before they are registered with Guice.  Implementations can remove or add modules
    * as wanted.  The returned modules will be used to create the injector.  The default implementation does not modify
    * the passed in modules.
    *
    * @param modules all configured modules
    * @return the modules that should be used to create the injector
    */
   protected Collection<Module> filterAllModules(Collection<Module> modules) {
      return modules;
   }
}
