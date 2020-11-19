/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.jellyfish.impl.provider;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.api.ICommandProvider;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.parameter.api.IParameterService;
import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;

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
