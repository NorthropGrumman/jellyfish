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
