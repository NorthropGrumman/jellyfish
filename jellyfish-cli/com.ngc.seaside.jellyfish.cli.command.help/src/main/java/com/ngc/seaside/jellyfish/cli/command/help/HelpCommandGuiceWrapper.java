package com.ngc.seaside.jellyfish.cli.command.help;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.api.JellyFishCommandConfiguration;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Singleton
@JellyFishCommandConfiguration(requireValidSystemDescriptor = false)
public class HelpCommandGuiceWrapper implements IJellyFishCommand {

   private final HelpCommand delegate = new HelpCommand();

   /**
    * The commands as injected by Guice.  Note some of these may not be real commands but Guice created proxies.  See
    * the note in the constructor.
    */
   private final Set<IJellyFishCommand> commandProxies;

   /**
    * If true, commands have been injected into the delegate.  If false, they have not been.
    */
   private final AtomicBoolean areCommandsInjected = new AtomicBoolean(false);

   @Inject
   public HelpCommandGuiceWrapper(ILogService logService,
                                  Set<IJellyFishCommand> commands) {
      delegate.setLogService(logService);
      // Note we can't call commands.forEach(delegate::addCommand) because they may throw a Guice exception if a
      // command requires the IJellyFishCommandProvider to be injected into it.  If this is the case, Guice creates a
      // proxy for the command but it would let us use the proxy until all injection is completed.
      commandProxies = commands;
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
   public void run(IJellyFishCommandOptions commandOptions) {
      injectCommandsIfNeeded();
      delegate.run(commandOptions);
   }

   private void injectCommandsIfNeeded() {
      if (areCommandsInjected.compareAndSet(false, true)) {
         commandProxies.forEach(delegate::addCommand);
      }
   }
}
