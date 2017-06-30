package com.ngc.seaside.bootstrap.impl.provider;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.api.IBootstrapCommand;
import com.ngc.seaside.bootstrap.api.IBootstrapCommandProvider;
import com.ngc.seaside.bootstrap.service.parameter.api.IParameterService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.command.api.IUsage;

import java.util.Set;

/**
 *
 */
@Singleton
public class BootstrapCommandProviderGuiceWrapper implements IBootstrapCommandProvider {
   private final BootstrapCommandProvider delegate = new BootstrapCommandProvider();

   @Inject
   public BootstrapCommandProviderGuiceWrapper(ILogService logService,
                                               ITemplateService templateService,
                                               IParameterService parameterService,
                                               Set<IBootstrapCommand> commands) {
      delegate.setLogService(logService);
      delegate.setTemplateService(templateService);
      delegate.setParameterService(parameterService);
      commands.forEach(delegate::addCommand);
      delegate.activate();
   }

   @Override
   public IUsage getUsage() {
      return delegate.getUsage();
   }

   @Override
   public void addCommand(IBootstrapCommand command) {
      delegate.addCommand(command);
   }

   @Override
   public void removeCommand(IBootstrapCommand command) {
      delegate.removeCommand(command);
   }

   @Override
   public void run(String[] arguments) {
      delegate.run(arguments);
   }
}
