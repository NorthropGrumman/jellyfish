package com.ngc.seaside.jellyfish.cli.command.validate;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;

public class ValidateCommandGuiceWrapper implements IJellyFishCommand {

   private final ValidateCommand delegate = new ValidateCommand();

   @Inject
   public ValidateCommandGuiceWrapper(ILogService logService) {
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
   public boolean requiresValidSystemDescriptorProject() {
      return delegate.requiresValidSystemDescriptorProject();
   }

   @Override
   public void run(IJellyFishCommandOptions commandOptions) {
      delegate.run(commandOptions);
   }

}
