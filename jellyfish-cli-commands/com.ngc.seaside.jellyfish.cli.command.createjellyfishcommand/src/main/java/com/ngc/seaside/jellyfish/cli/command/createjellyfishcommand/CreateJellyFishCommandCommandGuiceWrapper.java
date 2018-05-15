package com.ngc.seaside.jellyfish.cli.command.createjellyfishcommand;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;

public class CreateJellyFishCommandCommandGuiceWrapper implements ICommand<ICommandOptions> {

   private final CreateJellyFishCommandCommand delegate = new CreateJellyFishCommandCommand();

   @Inject
   public CreateJellyFishCommandCommandGuiceWrapper(ILogService logService, ITemplateService templateService) {
      delegate.setLogService(logService);
      delegate.setTemplateService(templateService);
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
   public void run(ICommandOptions commandOptions) {
      delegate.run(commandOptions);
   }

}
