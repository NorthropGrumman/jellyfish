package com.ngc.seaside.jellyfish.impl.provider;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.parameter.api.IParameterService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 */
@Singleton
public class JellyFishCommandProviderGuiceWrapper implements IJellyFishCommandProvider {

   /**
    * The delegate.
    */
   private final JellyFishCommandProvider delegate = new JellyFishCommandProvider();

   /**
    * The commands as injected by Guice.  Note some of these may not be real commands but Guice created proxies.  See
    * the note in the constructor.
    */
   private final Set<IJellyFishCommand> commandProxies;

   /**
    * If true, commands have been injected into the delegate.  If false, they haven been.
    */
   private final AtomicBoolean areCommandsInjected = new AtomicBoolean(false);

   @Inject
   public JellyFishCommandProviderGuiceWrapper(
         ILogService logService,
         ITemplateService templateService,
         IPromptUserService promptService,
         IParameterService parameterService,
         ISystemDescriptorService systemDescriptorService,
         Set<IJellyFishCommand> commands) {
      delegate.setLogService(logService);
      delegate.setParameterService(parameterService);
      delegate.setTemplateService(templateService);
      delegate.setPromptService(promptService);
      delegate.setSystemDescriptorService(systemDescriptorService);
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
   public IJellyFishCommand getCommand(String commandName) {
      injectCommandsIfNeeded();
      return delegate.getCommand(commandName);
   }

   @Override
   public void addCommand(IJellyFishCommand command) {
      injectCommandsIfNeeded();
      delegate.addCommand(command);
   }

   @Override
   public void removeCommand(IJellyFishCommand command) {
      injectCommandsIfNeeded();
      delegate.removeCommand(command);
   }

   @Override
   public void run(String[] arguments) {
      injectCommandsIfNeeded();
      delegate.run(arguments);
   }

   @Override
   public void run(String command, IJellyFishCommandOptions commandOptions) {
      injectCommandsIfNeeded();
      delegate.run(command, commandOptions);
   }

   private void injectCommandsIfNeeded() {
      if (areCommandsInjected.compareAndSet(false, true)) {
         commandProxies.forEach(delegate::addCommand);
      }
   }
}
