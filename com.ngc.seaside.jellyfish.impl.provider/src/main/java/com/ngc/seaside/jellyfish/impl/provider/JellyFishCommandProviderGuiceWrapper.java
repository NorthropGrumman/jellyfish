package com.ngc.seaside.jellyfish.impl.provider;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.parameter.api.IParameterService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;

import java.util.Set;

/**
 *
 */
@Singleton
public class JellyFishCommandProviderGuiceWrapper implements IJellyFishCommandProvider {

   private final JellyFishCommandProvider delegate = new JellyFishCommandProvider();

   @Inject
   public JellyFishCommandProviderGuiceWrapper(
            ILogService logService,
            ITemplateService templateService,
            IParameterService parameterService,
            ISystemDescriptorService systemDescriptorService,
            Set<IJellyFishCommand> commands) {
      delegate.setLogService(logService);
      delegate.setParameterService(parameterService);
      delegate.setTemplateService(templateService);
      delegate.setSystemDescriptorService(systemDescriptorService);
      commands.forEach(delegate::addCommand);
      delegate.activate();
   }

   @Override
   public IUsage getUsage() {
      return delegate.getUsage();
   }

   @Override
   public IJellyFishCommand getCommand(String commandName) {
      return delegate.getCommand(commandName);
   }

   @Override
   public void addCommand(IJellyFishCommand command) {
      delegate.addCommand(command);
   }

   @Override
   public void removeCommand(IJellyFishCommand command) {
      delegate.removeCommand(command);
   }

   @Override
   public void run(String[] arguments) {
      delegate.run(arguments);
   }
}
