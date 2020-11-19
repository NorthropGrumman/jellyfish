/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.jellyfish.cli.gradle;

import java.util.Arrays;
import java.util.Collection;

import com.google.inject.Module;
import com.ngc.seaside.jellyfish.DefaultJellyfishModule;
import com.ngc.seaside.jellyfish.Log4J2Module;
import com.ngc.seaside.jellyfish.cli.gradle.adapter.ClasspathTemplateService;
import com.ngc.seaside.jellyfish.cli.gradle.adapter.GradleLogService;
import com.ngc.seaside.jellyfish.service.impl.templateservice.TemplateServiceGuiceModule;

/**
 * The module used to run Jellyfish within Gradle.
 */
public class GradleJellyfishModule extends DefaultJellyfishModule {

   private static final Collection<Class<?>> UNWANTED_MODULES = Arrays.asList(
         TemplateServiceGuiceModule.class,
         Log4J2Module.class
   );

   @Override
   protected Collection<Module> configureCustomModules(Collection<Module> modules) {
      modules.add(ClasspathTemplateService.MODULE);
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
