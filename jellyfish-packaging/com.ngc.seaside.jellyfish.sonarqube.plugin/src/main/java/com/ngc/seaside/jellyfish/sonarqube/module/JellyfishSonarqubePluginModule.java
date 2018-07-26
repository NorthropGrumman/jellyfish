package com.ngc.seaside.jellyfish.sonarqube.module;

import com.google.common.base.Preconditions;
import com.google.inject.Module;

import com.ngc.blocs.guice.module.LogServiceModule;
import com.ngc.seaside.jellyfish.DefaultJellyfishModule;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;

/**
 * A Guava module that register all components need to use Jellyfish and parse System Descriptor files.
 */
public class JellyfishSonarqubePluginModule extends DefaultJellyfishModule {

   @Override
   public Collection<Module> filterAllModules(Collection<Module> modules) {
      modules.removeIf(m -> m.getClass() == LogServiceModule.class);
      modules.add(new SonarqubeLogServiceModule());
      return modules;
   }

   @Override
   protected Collection<Module> configureModulesFromClasspath(Collection<Module> modules) {
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

      return modules;
   }
}
