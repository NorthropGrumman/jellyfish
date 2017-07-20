package com.ngc.seaside.jellyfish;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.jellyfish.impl.provider.JellyFishCommandProviderModule;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ServiceLoader;

public class JellyFish {

   private JellyFishCommandProviderModule jellyFishCommandProvider;
   private ProxyJellyFishCommandProvider proxyJellyFishCommandProvider = new ProxyJellyFishCommandProvider();

   /**
    * Main to run the JellyFish application..
    *
    * @param args the program arguments. The first argument should always be the name of the command in which to run
    *             followed by a list of parameters for that command.
    */
   public static void main(String[] args) {
      try {
         run(args);
      } catch (IllegalArgumentException e) {
         System.out.println("\nERROR: " + e.getMessage() + "\nTry running \"jellyfish help\" for usage information.");
      }
   }

   /**
    * Run the JellyFish application
    *
    * @param args the program arguments. The first argument should always be the name of the command in which to run
    *             followed by a list of parameters for that command.
    */
   public static void run(String[] args) {
      new JellyFish().doRun(args);
   }

   private void doRun(String[] args) {
      Injector injector = getInjector();
      jellyFishCommandProvider = injector.getInstance(JellyFishCommandProviderModule.class);
      jellyFishCommandProvider.run(args);
   }

   /**
    * @return the Guice injector
    */
   private Injector getInjector() {
      return Guice.createInjector(getModules());
   }

   /**
    * Get a collection of Guice modules from the classpath. The service loader will look for a
    * property file called com.google.inject.Module located in the META-INF/services directory
    * of the jar. The file just needs to list all Guice Module classes with the fully qualified name.
    *
    * @return A collection of modules or an empty collection.
    */
   private Collection<Module> getModules() {
      Collection<Module> modules = new ArrayList<>();
      modules.add(new JellyFishServiceModule());
      modules.add(proxyJellyFishCommandProvider);
      for (Module dynamicModule : ServiceLoader.load(Module.class)) {
         // TODO log this
         System.out.println(String.format("%s", dynamicModule.getClass()));
         modules.add(dynamicModule);
      }

      // The following code was added because multiple instances of the {@link XTextSystemDescriptorServiceModule}
      // were being created. This code can be removed once the system descriptor services have been updated
      // to handle this situation.
      modules.removeIf(m -> m instanceof XTextSystemDescriptorServiceModule);
      modules.add(XTextSystemDescriptorServiceModule.forStandaloneUsage());
      return modules;
   }

   private class ProxyJellyFishCommandProvider extends AbstractModule implements IJellyFishCommandProvider {

      @Override
      public void run(String command, IJellyFishCommandOptions commandOptions) {
         jellyFishCommandProvider.run(command, commandOptions);
      }

      @Override
      public IUsage getUsage() {
         return jellyFishCommandProvider.getUsage();
      }

      @Override
      public IJellyFishCommand getCommand(String commandName) {
         return jellyFishCommandProvider.getCommand(commandName);
      }

      @Override
      public void addCommand(IJellyFishCommand command) {
         jellyFishCommandProvider.addCommand(command);
      }

      @Override
      public void removeCommand(IJellyFishCommand command) {
         jellyFishCommandProvider.removeCommand(command);
      }

      @Override
      public void run(String[] arguments) {
         jellyFishCommandProvider.run(arguments);
      }

      @Override
      protected void configure() {
         bind(IJellyFishCommandProvider.class).toInstance(this);
      }
   }
}
