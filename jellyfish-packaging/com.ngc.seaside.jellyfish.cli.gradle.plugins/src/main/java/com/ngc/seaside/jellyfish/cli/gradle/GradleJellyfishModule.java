package com.ngc.seaside.jellyfish.cli.gradle;

import com.google.inject.Module;

import com.ngc.blocs.guice.module.LogServiceModule;
import com.ngc.blocs.guice.module.ResourceServiceModule;
import com.ngc.seaside.jellyfish.DefaultJellyfishModule;
import com.ngc.seaside.jellyfish.cli.gradle.adapter.ClasspathResourceService;
import com.ngc.seaside.jellyfish.cli.gradle.adapter.ClasspathTemplateService;
import com.ngc.seaside.jellyfish.cli.gradle.adapter.GradleLogService;
import com.ngc.seaside.jellyfish.service.impl.templateservice.TemplateServiceGuiceModule;

import java.util.Arrays;
import java.util.Collection;

/**
 * The module used to run Jellyfish within Gradle.
 */
public class GradleJellyfishModule extends DefaultJellyfishModule {

   private static final Collection<Class<?>> UNWANTED_MODULES = Arrays.asList(
         TemplateServiceGuiceModule.class,
         ResourceServiceModule.class,
         LogServiceModule.class
   );

   @Override
   protected Collection<Module> configureCustomModules(Collection<Module> modules) {
      modules.add(ClasspathTemplateService.MODULE);
      modules.add(ClasspathResourceService.MODULE);
      modules.add(GradleLogService.MODULE);
      return modules;
   }

   @Override
   protected Collection<Module> filterAllModules(Collection<Module> modules) {
      // Do not load the unwanted modules.
      modules.removeIf(m -> UNWANTED_MODULES.contains(m.getClass()));
      return modules;
   }
}
