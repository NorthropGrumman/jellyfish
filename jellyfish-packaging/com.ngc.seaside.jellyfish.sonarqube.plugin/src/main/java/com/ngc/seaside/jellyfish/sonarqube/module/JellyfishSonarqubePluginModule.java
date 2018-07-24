package com.ngc.seaside.jellyfish.sonarqube.module;

import com.google.inject.Module;

import com.ngc.blocs.guice.module.LogServiceModule;
import com.ngc.seaside.jellyfish.DefaultJellyfishModule;
import com.ngc.seaside.jellyfish.sonarqube.JellyfishPlugin;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.Collection;

/**
 * A Guava module that register all components need to use Jellyfish and parse System Descriptor files.
 */
public class JellyfishSonarqubePluginModule extends DefaultJellyfishModule {

   // TODO TH: Remove this
   private static final Logger LOGGER = Loggers.get(JellyfishPlugin.class);

   @Override
   public Collection<Module> filterAllModules(Collection<Module> modules) {
      modules.removeIf(m -> m.getClass() == LogServiceModule.class);
      modules.add(new SonarqubeLogServiceModule());
      // TODO TH: remove this
      modules.forEach(m -> LOGGER.info("!@!@ Will include module {} {}", m.getClass().getName(), m));
      return modules;
   }
}
