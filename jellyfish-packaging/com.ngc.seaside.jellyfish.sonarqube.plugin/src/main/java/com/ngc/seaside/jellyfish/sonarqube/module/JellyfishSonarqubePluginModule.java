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
package com.ngc.seaside.jellyfish.sonarqube.module;

import com.google.common.base.Preconditions;
import com.google.inject.Module;

import com.ngc.seaside.jellyfish.DefaultJellyfishModule;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.function.Predicate;

/**
 * The Jellyfish module for the Sonarqube plugin.  This module allows loading default Jellyfish modules by sourcing the
 * file "guice-modules" from the classpath.
 *
 * <p>
 * Users creating custom Sonarqube plugins rarely modify or extend this class.
 */
public class JellyfishSonarqubePluginModule extends DefaultJellyfishModule {

   /**
    * If true, the default Jellyfish modules should be loaded.
    */
   private boolean includeDefaultModules = true;

   /**
    * Used to excludes modules.  THe default excludes no modules.
    */
   private Predicate<Module> filter = m -> false;

   /**
    * Sets if the default Jellyfish modules should be installed by this module.  If this value is false, users must
    * ensure {@link com.ngc.seaside.jellyfish.sonarqube.extension.IJellyfishModuleFactory#getJellyfishModules(boolean)}
    * returns <i>all</i> modules needed by Jellyfish.  Otherwise Jellyfish will not execute correctly.
    *
    * @param includeDefaultModules true if the default Jellyfish modules should be included, false otherwise
    * @return this plugin
    */
   public JellyfishSonarqubePluginModule setIncludeDefaultModules(boolean includeDefaultModules) {
      this.includeDefaultModules = includeDefaultModules;
      return this;
   }

   /**
    * Sets the filter used to remove unwanted modules includes in this configuration.  If the filter returns true for
    * any module, that module is executed.
    *
    * @param filter the predicate to use to execute modules
    * @return this module
    */
   public JellyfishSonarqubePluginModule setFilter(Predicate<Module> filter) {
      this.filter = Preconditions.checkNotNull(filter, "filter may not be null!");
      return this;
   }

   @Override
   protected Collection<Module> filterAllModules(Collection<Module> modules) {
      modules.removeIf(filter);
      return modules;
   }

   @Override
   protected Collection<Module> configureModulesFromClasspath(Collection<Module> modules) {
      if (includeDefaultModules) {
         // We can't use the default behavior in DefaultJellyfishModule because of how Sonarqube sets up the classpath
         // and classloader for plugins.  Thus we do this extra logic to figure out which Guice modules to include.
         InputStream is = JellyfishSonarqubePluginModule.class.getClassLoader().getResourceAsStream("guice-modules");
         Preconditions.checkState(is != null,
                                  "failed to load file guice-modules from classpath!");
         try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line = br.readLine();
            while (line != null) {
               Module m = (Module) JellyfishSonarqubePluginModule.class.getClassLoader().loadClass(line).newInstance();
               if (m.getClass() != XTextSystemDescriptorServiceModule.class) {
                  modules.add(m);
               }
               line = br.readLine();
            }
         } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("failed to create instance of a required Guice module!", e);
         }
      }

      return modules;
   }
}
