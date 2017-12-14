package com.ngc.seaside.bootstrap.command.impl.help;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.api.IBootstrapCommand;
import com.ngc.seaside.bootstrap.api.IBootstrapCommandOptions;
import com.ngc.seaside.command.api.IUsage;

/**
 * Wrap the service using Guice Injection
 */
@Singleton
public class HelpCommandGuiceWrapper implements IBootstrapCommand {

   private final HelpCommand delegate = new HelpCommand();

   @Inject
   public void setLogService(ILogService logService) {
      delegate.setLogService(logService);
      delegate.activate();
   }

   @Override
   public String getName() {
      return delegate.getName();
   }

   @Override
   public IUsage getUsage() {
      return delegate.getUsage();
   }

   @Override
   public void run(IBootstrapCommandOptions commandOptions) {
      delegate.run(commandOptions);
   }
}
