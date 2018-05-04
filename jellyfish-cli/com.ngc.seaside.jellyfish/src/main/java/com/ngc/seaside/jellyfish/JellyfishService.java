package com.ngc.seaside.jellyfish;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.jellyfish.service.execution.api.IJellyfishExecution;
import com.ngc.seaside.jellyfish.service.execution.api.IJellyfishService;
import com.ngc.seaside.jellyfish.service.execution.api.JellyfishExecutionException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * An implementation of the {@code IJellyfishService} that can be referenced from other applications.  This service
 * runs with Guice and will create a new Injector for every execution of Jellyfish.
 */
public class JellyfishService implements IJellyfishService {

   @Override
   public IJellyfishExecution run(String command, Collection<String> arguments, Collection<Module> modules)
         throws JellyfishExecutionException {
      Preconditions.checkNotNull(command, "command may not be null!");
      Preconditions.checkArgument(!command.trim().isEmpty(), "command may not be empty!");
      Preconditions.checkNotNull(arguments, "arguments may not be null!");
      Preconditions.checkNotNull(modules, "modules may not be null!");

      Collection<Module> mods = new ArrayList<>(modules);
      // Add a module that register this service with the rest of Guice.
      mods.add(new SelfRegisteringModule());

      // TODO TH: Set NG_FW_HOME variable here.
      try {
         Injector injector = createInjector(modules);
         // Get the command provider and run.
         IJellyFishCommandProvider provider = injector.getInstance(IJellyFishCommandProvider.class);
         return adaptResult(provider.run(buildArgs(command, arguments)));
      } catch (Throwable t) {
         String msg = String.format("unable to run Jellyfish with the command %s and args %s!",
                                    command,
                                    arguments);
         throw new JellyfishExecutionException(msg, t);
      }
   }

   @Override
   public IJellyfishExecution run(String command, Map<String, String> arguments, Collection<Module> modules)
         throws JellyfishExecutionException {
      Preconditions.checkNotNull(command, "command may not be null!");
      Preconditions.checkArgument(!command.trim().isEmpty(), "command may not be empty!");
      Preconditions.checkNotNull(arguments, "arguments may not be null!");
      Preconditions.checkNotNull(modules, "modules may not be null!");
      Collection<String> args = arguments.entrySet()
            .stream()
            .map(e -> e.getKey() + "=" + e.getValue())
            .collect(Collectors.toList());
      return run(command, args, modules);
   }

   /**
    * Invoked to create an injector.
    *
    * @param modules the modules to create the injector with
    * @return the injector
    */
   protected Injector createInjector(Collection<Module> modules) {
      return Guice.createInjector(Stage.PRODUCTION, modules);
   }

   /**
    * Adapts an {@code IJellyFishCommandOptions} that comes back from the provider to an {@code IJellyfishExecution}.
    *
    * @param options the result to adapt
    * @return the adapted result
    */
   protected IJellyfishExecution adaptResult(IJellyFishCommandOptions options) {
      return new JellyfishExecution(options);
   }

   private static String[] buildArgs(String command, Collection<String> arguments) {
      String[] args = new String[arguments.size() + 1];
      args[0] = command;
      int x = 1;
      for (String argument : arguments) {
         args[x] = argument;
         x++;
      }
      return args;
   }

   /**
    * A module that allows this service to register itself in the injector.  This allows this service to be injected
    * into other components.
    */
   private class SelfRegisteringModule extends AbstractModule {

      @Override
      protected void configure() {
         bind(IJellyfishService.class).toInstance(JellyfishService.this);
      }
   }

   /**
    * Default implementation of {@code IJellyfishExecution}.
    */
   private static class JellyfishExecution implements IJellyfishExecution {

      private final IJellyFishCommandOptions options;

      private JellyfishExecution(IJellyFishCommandOptions options) {
         this.options = options;
      }

      @Override
      public IJellyFishCommandOptions getOptions() {
         return options;
      }
   }
}
