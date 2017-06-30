package com.ngc.seaside.bootstrap;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import com.ngc.seaside.bootstrap.impl.provider.BootstrapCommandProviderGuiceWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ServiceLoader;

/**
 *
 */
public class Main {

   /**
    * Run the Main application.
    *
    * @param args the program arguments. The first argument should always be the name of the command in which to run
    *             followed by a list of parameters for that command.
    */
   public static void main(String[] args) {
      Injector injector = Guice.createInjector(getModules());
      BootstrapCommandProviderGuiceWrapper provider = injector.getInstance(BootstrapCommandProviderGuiceWrapper.class);
      provider.run(args);
   }

   /**
    * Get a collection of Guice modules from the classpath. The service loader will look for a
    * property file called com.google.inject.Module located in the META-INF/services directory
    * of the jar. The file just needs to list all Guice Module classes with the fully qualified name.
    *
    * @return A collection of modules or an empty collection.
    */
   private static Collection<Module> getModules() {
      Collection<Module> modules = new ArrayList<>();

      for (Module dynamicModule : ServiceLoader.load(Module.class)) {
         System.out.println(String.format("%s", dynamicModule.getClass()));
         modules.add(dynamicModule);
      }
      return modules;
   }


}
