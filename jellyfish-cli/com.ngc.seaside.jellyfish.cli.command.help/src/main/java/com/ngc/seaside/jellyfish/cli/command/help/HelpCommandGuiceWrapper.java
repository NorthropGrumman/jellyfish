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
package com.ngc.seaside.jellyfish.cli.command.help;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.jellyfish.api.IUsage;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Singleton
public class HelpCommandGuiceWrapper implements ICommand<ICommandOptions> {

   private final HelpCommand delegate = new HelpCommand();

   /**
    * We need the injector to get all the commands.  We can't inject the commands directly because this component is
    * also a command and it causes Guice to throw errors abound circular dependencies.
    */
   private final Injector injector;

   /**
    * If true, commands have been injected into the delegate.  If false, they have not been.
    */
   private final AtomicBoolean areCommandsInjected = new AtomicBoolean(false);

   @Inject
   public HelpCommandGuiceWrapper(Injector injector, ILogService logService) {
      this.injector = injector;
      delegate.setLogService(logService);
      delegate.activate();
   }

   @Override
   public String getName() {
      injectCommandsIfNeeded();
      return delegate.getName();
   }

   @Override
   public IUsage getUsage() {
      injectCommandsIfNeeded();
      return delegate.getUsage();
   }

   @Override
   public void run(ICommandOptions commandOptions) {
      injectCommandsIfNeeded();
      delegate.run(commandOptions);
   }

   @SuppressWarnings("rawtypes")
   private void injectCommandsIfNeeded() {
      if (areCommandsInjected.compareAndSet(false, true)) {
         TypeLiteral<Set<IJellyFishCommand>> jfCommandsType = new TypeLiteral<Set<IJellyFishCommand>>() {
         };
         injector.getInstance(Key.get(jfCommandsType)).forEach(delegate::addCommand);
         TypeLiteral<Set<ICommand>> commandsType = new TypeLiteral<Set<ICommand>>() {
         };
         injector.getInstance(Key.get(commandsType)).forEach(delegate::addCommand);
         delegate.setJellyfishProvider(injector.getInstance(IJellyFishCommandProvider.class));
      }
   }
}
