package com.ngc.seaside.bootstrap;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import com.ngc.seaside.bootstrap.impl.provider.BootstrapCommandProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ServiceLoader;

/**
 * @author justan.provence@ngc.com
 */
public class Bootstrap {

   /**
    * Run the Bootstrap application.
    *
    * @param args the program arguments. The first argument should always be the name of the
    *             command in which to run followed by a list of parameters for that command.
    */
   public static void main(String[] args) {
      Injector injector = Guice.createInjector(getModules());
      BootstrapCommandProvider provider = injector.getInstance(BootstrapCommandProvider.class);
      provider.run(args);
   }

   /**
    * Get a collection of Guice modules from the classpath. The service loader will look for a
    * property file called com.google.inject.Module located in the META-INF/services directory
    * of the jar. The file just needs to list all Guice Module classes with the fully qualified name.
    * @return A collection of modules or an empty collection.
    */
   private static Collection<Module> getModules() {
      Collection<Module> modules = new ArrayList<>();
      modules.add(new BLoCSServiceModule());
      for (Module dynamicModule : ServiceLoader.load(Module.class)) {
         modules.add(dynamicModule);
      }
      return modules;
   }
}
