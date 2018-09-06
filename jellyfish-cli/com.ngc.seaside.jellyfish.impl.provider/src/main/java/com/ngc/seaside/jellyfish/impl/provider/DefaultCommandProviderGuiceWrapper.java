/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish.impl.provider;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.api.ICommandProvider;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.parameter.api.IParameterService;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Wraps the default command provider for use with Guice.
 */
@Singleton
@SuppressWarnings({"rawtypes"})
public class DefaultCommandProviderGuiceWrapper implements ICommandProvider<
      ICommandOptions,
      ICommand<ICommandOptions>,
      ICommandOptions> {

   /**
    * The delegate.
    */
   private final DefaultCommandProvider delegate = new DefaultCommandProvider();

   /**
    * The commands as injected by Guice.  Note some of these may not be real commands but Guice created proxies.  See
    * the note in the constructor.
    */
   private final Set<ICommand> commandProxies;

   /**
    * If true, commands have been injected into the delegate.  If false, they haven been.
    */
   private final AtomicBoolean areCommandsInjected = new AtomicBoolean(false);

   @Inject
   public DefaultCommandProviderGuiceWrapper(ILogService logService,
                                             IParameterService parameterService,
                                             Set<ICommand> commands) {
      delegate.setLogService(logService);
      delegate.setParameterService(parameterService);
      // Note we can't call commands.forEach(delegate::addCommand) because they may throw a Guice exception if a
      // command requires the IJellyFishCommandProvider to be injected into it.  If this is the case, Guice creates a
      // proxy for the command but it would let us use the proxy until all injection is completed.
      commandProxies = commands;
      delegate.activate();
   }

   @Override
   public IUsage getUsage() {
      injectCommandsIfNeeded();
      return delegate.getUsage();
   }

   @Override
   public ICommandOptions run(String[] arguments) {
      injectCommandsIfNeeded();
      return delegate.run(arguments);
   }

   @Override
   public void run(String command, ICommandOptions commandOptions) {
      injectCommandsIfNeeded();
      delegate.run(command, commandOptions);
   }

   @Override
   public ICommand<ICommandOptions> getCommand(String commandName) {
      injectCommandsIfNeeded();
      return delegate.getCommand(commandName);
   }

   @Override
   public void addCommand(ICommand<ICommandOptions> command) {
      injectCommandsIfNeeded();
      delegate.addCommand(command);
   }

   @Override
   public void removeCommand(ICommand<ICommandOptions> command) {
      injectCommandsIfNeeded();
      delegate.removeCommand(command);
   }

   @SuppressWarnings("unchecked")
   private void injectCommandsIfNeeded() {
      if (areCommandsInjected.compareAndSet(false, true)) {
         for (ICommand proxy : commandProxies) {
            delegate.addCommand(proxy);
         }
      }
   }
}
