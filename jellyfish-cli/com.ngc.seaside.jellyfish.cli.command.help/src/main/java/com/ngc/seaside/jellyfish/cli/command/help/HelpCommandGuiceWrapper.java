package com.ngc.seaside.jellyfish.cli.command.help;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Singleton
public class HelpCommandGuiceWrapper implements IJellyFishCommand {

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
   public boolean requiresValidSystemDescriptorProject() {
      return delegate.requiresValidSystemDescriptorProject();
   }

   @Override
   public void run(IJellyFishCommandOptions commandOptions) {
      injectCommandsIfNeeded();
      delegate.run(commandOptions);
   }

   private void injectCommandsIfNeeded() {
      if (areCommandsInjected.compareAndSet(false, true)) {
         TypeLiteral<Set<IJellyFishCommand>> type = new TypeLiteral<Set<IJellyFishCommand>>() {
         };
         injector.getInstance(Key.get(type)).forEach(delegate::addCommand);
      }
   }
}
