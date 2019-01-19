/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;

import com.ngc.seaside.jellyfish.api.DefaultJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.api.ICommandProvider;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.jellyfish.service.execution.api.IJellyfishExecution;
import com.ngc.seaside.jellyfish.service.execution.api.IJellyfishService;
import com.ngc.seaside.jellyfish.service.execution.api.JellyfishExecutionException;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * An implementation of the {@code IJellyfishService} that can be referenced from other applications.  This service
 * runs with Guice and will create a new Injector for every execution of Jellyfish.
 */
public class JellyfishService implements IJellyfishService {

   /**
    * The name of the system property that BLoCS uses to find the resources to load at runtime.  The value is
    * an absolute path that cannot contain relative paths (like . or ..).
    */
   static final String BLOCS_HOME_SYSTEM_PROPERTY = "NG_FW_HOME";

   @Override
   public IJellyfishExecution run(String command, Collection<String> arguments, Collection<Module> modules)
         throws JellyfishExecutionException {
      Stopwatch sw = Stopwatch.createStarted();
      Preconditions.checkNotNull(command, "command may not be null!");
      Preconditions.checkArgument(!command.trim().isEmpty(), "command may not be empty!");
      Preconditions.checkNotNull(arguments, "arguments may not be null!");
      Preconditions.checkNotNull(modules, "modules may not be null!");

      Collection<Module> mods = new ArrayList<>(modules);
      // Add a module that register this service with the rest of Guice.
      mods.add(new SelfRegisteringModule());

      boolean isBlocsHomeSet = System.getProperty(BLOCS_HOME_SYSTEM_PROPERTY) != null;
      try {
         // Set the BLoCS home property if needed.
         if (!isBlocsHomeSet) {
            System.setProperty(BLOCS_HOME_SYSTEM_PROPERTY, getDefaultBlocsHome());
         }

         Injector injector = createInjector(mods);
         return runCommand(injector, command, arguments, sw);
      } catch (Throwable t) {
         String msg = String.format("unable to run Jellyfish with the command %s and args %s!",
                                    command,
                                    arguments);
         throw new JellyfishExecutionException(msg, t);
      } finally {
         // If we set the property, clear it before finishing.
         if (!isBlocsHomeSet) {
            System.clearProperty(BLOCS_HOME_SYSTEM_PROPERTY);
         }
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
    * Adapts an {@code ICommandOptions} that comes back from the provider to an {@code IJellyfishExecution}.
    *
    * @param options           the result to adapt
    * @param executionDuration the time taken to run Jellyfish
    * @param injector the injector used to run Jellyfish
    * @return the adapted result
    */
   protected IJellyfishExecution adaptResult(ICommandOptions options,
                                             long executionDuration,
                                             Injector injector) {
      return new JellyfishExecution(options)
            .setExecutionDuration(executionDuration)
            .setInjector(injector);
   }

   /**
    * Adapts an {@code IJellyFishCommandOptions} that comes back from the provider to an {@code IJellyfishExecution}.
    *
    * @param options           the result to adapt
    * @param executionDuration the time taken to run Jellyfish
    * @param injector the injector used to run Jellyfish
    * @return the adapted result
    */
   protected IJellyfishExecution adaptResult(IJellyFishCommandOptions options,
                                             long executionDuration,
                                             Injector injector) {
      return new JellyfishExecution(options)
            .setExecutionDuration(executionDuration)
            .setInjector(injector);
   }

   private IJellyfishExecution runCommand(Injector injector,
                                          String command,
                                          Collection<String> arguments,
                                          Stopwatch sw) {
      // Determine which type of provider handles this command.
      IJellyFishCommandProvider jfProvider = injector.getInstance(IJellyFishCommandProvider.class);
      @SuppressWarnings({"unchecked"})
      ICommandProvider<ICommandOptions, ICommand<ICommandOptions>, ICommandOptions> defaultProvider =
            injector.getInstance(ICommandProvider.class);
      // Is this a Jellyfish command which requires an SD project?
      if (jfProvider.getCommand(command) != null) {
         return adaptResult(jfProvider.run(buildArgs(command, arguments)),
                            sw.elapsed(TimeUnit.MILLISECONDS),
                            injector);
      } else {
         // Otherwise, this must be a default command that does not require an SD project.
         return adaptResult(defaultProvider.run(buildArgs(command, arguments)),
                            sw.elapsed(TimeUnit.MILLISECONDS),
                            injector);
      }
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

   private static String getDefaultBlocsHome() {
      return Paths.get(System.getProperty("user.dir")).toAbsolutePath().toString();
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
    * Default implementation of {@code IJellyfishExecution}.  Can adapt either a {@code IJellyFishCommandOptions} or a
    * {@code ICommandOptions}.
    */
   private static class JellyfishExecution implements IJellyfishExecution {

      private final IJellyFishCommandOptions options;
      private Injector injector;
      private long executionDuration;

      private JellyfishExecution(IJellyFishCommandOptions options) {
         this.options = options;
      }

      private JellyfishExecution(ICommandOptions basicOptions) {
         DefaultJellyFishCommandOptions options = new DefaultJellyFishCommandOptions();
         options.setParameters(basicOptions.getParameters());
         this.options = options;
      }

      @Override
      public Injector getInjector() {
         return injector;
      }

      @Override
      public IJellyFishCommandOptions getOptions() {
         return options;
      }

      @Override
      public long getExecutionDuration() {
         return executionDuration;
      }

      JellyfishExecution setExecutionDuration(long executionDuration) {
         this.executionDuration = executionDuration;
         return this;
      }

      JellyfishExecution setInjector(Injector injector) {
         this.injector = injector;
         return this;
      }
   }
}
