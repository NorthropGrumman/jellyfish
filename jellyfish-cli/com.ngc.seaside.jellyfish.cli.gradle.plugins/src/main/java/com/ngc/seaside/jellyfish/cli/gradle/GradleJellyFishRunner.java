package com.ngc.seaside.jellyfish.cli.gradle;

import com.google.inject.Module;

import com.ngc.blocs.guice.module.ResourceServiceModule;
import com.ngc.seaside.jellyfish.JellyFish;
import com.ngc.seaside.jellyfish.JellyFishServiceModule;
import com.ngc.seaside.jellyfish.cli.gradle.adapter.ClasspathResourceService;
import com.ngc.seaside.jellyfish.cli.gradle.adapter.ClasspathTemplateService;
import com.ngc.seaside.jellyfish.service.impl.templateservice.TemplateServiceGuiceModule;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

public class GradleJellyFishRunner {

   private final static Set<Class<? extends Module>> MODULES_TO_IGNORE;

   static {
      Set<Class<? extends Module>> ignoring = new HashSet<>();
      // Do no load these modules if they are present.
      ignoring.add(XTextSystemDescriptorServiceModule.class);
      ignoring.add(TemplateServiceGuiceModule.class);
      ignoring.add(ResourceServiceModule.class);
      MODULES_TO_IGNORE = Collections.unmodifiableSet(ignoring);
   }

   public static void run(String[] args) {
      JellyFish.run(args, getModules());
   }

   private static Collection<Module> getModules() {
      Collection<Module> modules = new ArrayList<>();
      modules.add(new JellyFishServiceModule());
      for (Module dynamicModule : ServiceLoader.load(Module.class)) {
         // Ignore the XTextSystemDescriptorServiceModule, we'll create the module below via forStandaloneUsage().  This
         // is because XTextSystemDescriptorServiceModule is registered as a module and the service loader picks it up.
         // However, we need to build the module via forStandaloneUsage() to make sure the XText framework is
         // initialized correctly.
         if (!MODULES_TO_IGNORE.contains(dynamicModule.getClass())) {
            modules.add(dynamicModule);
         }
      }
      // Register the standalone version of the XText service.
      modules.add(XTextSystemDescriptorServiceModule.forStandaloneUsage());
      // Register the custom services used for gradle builds.
      modules.add(ClasspathTemplateService.MODULE);
      modules.add(ClasspathResourceService.MODULE);
      return modules;
   }

}
