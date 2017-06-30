package com.ngc.seaside.bootstrap.command.impl.help;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.multibindings.Multibinder;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.api.IBootstrapCommand;
import com.ngc.seaside.bootstrap.api.IBootstrapCommandOptions;
import com.ngc.seaside.command.api.ICommand;
import com.ngc.seaside.command.api.IUsage;

/**
 *
 */
public class HelpCommandModule extends AbstractModule implements IBootstrapCommand {

   private final HelpCommand delegate = new HelpCommand();

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IBootstrapCommand.class)
               .addBinding()
               .to(HelpCommand.class);

      Multibinder.newSetBinder(binder(), ICommand.class)
               .addBinding()
               .to(HelpCommand.class);
   }

   @Inject
   public void setLogService(ILogService logService) {
      delegate.setLogService(logService);
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
