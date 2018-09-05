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
