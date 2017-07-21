package com.ngc.seaside.jellyfish;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import com.ngc.blocs.service.log.api.ILogService;
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

   /**
    * An implementation of {@code IJellyFishCommandProvider} that delegates to the field {@code
    * jellyFishCommandProvider}.  This proxy will only work if {@code jellyFishCommandProvider} is not {@code null}.
    * The field will be {@code null} until the real implementation is resolved from the injector.
    */
   private final ProxyJellyFishCommandProvider proxyJellyFishCommandProvider = new ProxyJellyFishCommandProvider();

   /**
    * The real implementation of the {@code IJellyFishCommandProvider}.
    */
   private IJellyFishCommandProvider jellyFishCommandProvider;

   private ILogService logService;

   /**
    * Main to run the JellyFish application.
    *
    * @param args the program arguments. The first argument should always be the name of the command in which to run
    *             followed by a list of parameters for that command.
    */
   public static void main(String[] args) {
      try {
         run(args);
      } catch (IllegalArgumentException e) {
         System.err.format("%nERROR: %s%nTry running \"jellyfish help\" for usage information.%n", e.getMessage());
      }
   }

   /**
    * Run the JellyFish application.
    *
    * @param args the program arguments. The first argument should always be the name of the command in which to run
    *             followed by a list of parameters for that command.
    */
   public static void run(String[] args) {
      new JellyFish().doRun(args);
   }

   private void doRun(String[] args) {
      Injector injector = getInjector();
      // Note the module itself is an implementation of IJellyFishCommandProvider.
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
      // Add the proxy command provider module so that an IJellyFishCommandProvider can be resolved.
      modules.add(proxyJellyFishCommandProvider);
      for (Module dynamicModule : ServiceLoader.load(Module.class)) {
         // Ignore the XTextSystemDescriptorServiceModule, we'll create the module below via forStandaloneUsage().  This
         // is because XTextSystemDescriptorServiceModule is registered as an Module and the service loader picks it up.
         // However, we need to build the module via forStandaloneUsage() to make sure the XText framework is
         // initialized correctly.
         if (dynamicModule.getClass() != XTextSystemDescriptorServiceModule.class) {
            logService.debug(getClass(), String.format("Loaded module %s.", dynamicModule.getClass()));
            modules.add(dynamicModule);
         }
      }
      // Register the standalone version of the XText service.
      modules.add(XTextSystemDescriptorServiceModule.forStandaloneUsage());
      return modules;
   }

   /**
    * A proxy of {@code IJellyFishCommandProvider} that will register itself with Guice.  This allows commands that are
    * injected into the real implementation to be able to resolve a reference to the this proxy implementation of the
    * command provider.  This provider can only be used after the injector has resolved the real implementation.  As
    * long as a command does not use the service until its run method is invoked, this won't be a problem.  Note this
    * is only an artifact of Guice; OSGi doesn't need this pattern.
    */
   private class ProxyJellyFishCommandProvider extends AbstractModule implements IJellyFishCommandProvider {

      @Override
      public void run(String command, IJellyFishCommandOptions commandOptions) {
         ensureProviderSet();
         jellyFishCommandProvider.run(command, commandOptions);
      }

      @Override
      public IUsage getUsage() {
         ensureProviderSet();
         return jellyFishCommandProvider.getUsage();
      }

      @Override
      public IJellyFishCommand getCommand(String commandName) {
         ensureProviderSet();
         return jellyFishCommandProvider.getCommand(commandName);
      }

      @Override
      public void addCommand(IJellyFishCommand command) {
         ensureProviderSet();
         jellyFishCommandProvider.addCommand(command);
      }

      @Override
      public void removeCommand(IJellyFishCommand command) {
         ensureProviderSet();
         jellyFishCommandProvider.removeCommand(command);
      }

      @Override
      public void run(String[] arguments) {
         ensureProviderSet();
         jellyFishCommandProvider.run(arguments);
      }

      @Override
      protected void configure() {
         // Bind IJellyFishCommandProvider to this proxy.
         bind(IJellyFishCommandProvider.class).toInstance(this);
      }

      private void ensureProviderSet() {
         Preconditions.checkState(
               jellyFishCommandProvider != null,
               "proxy is not fully resolved, please don't use this proxy until the run() method of the command is"
               + " called!");
      }
   }
}
