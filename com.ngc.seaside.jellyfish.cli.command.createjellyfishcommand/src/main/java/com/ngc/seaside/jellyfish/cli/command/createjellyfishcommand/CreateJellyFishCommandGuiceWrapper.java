package com.ngc.seaside.jellyfish.cli.command.createjellyfishcommand;

import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;

public class CreateJellyFishCommandGuiceWrapper implements IJellyFishCommand {
   
   private final CreateJellyFishCommand delegate = new CreateJellyFishCommand();

   @Inject
   public CreateJellyFishCommandGuiceWrapper(ILogService logService, IPromptUserService promptService) {
      delegate.setLogService(logService);
      delegate.setPromptService(promptService);
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
   public void run(IJellyFishCommandOptions commandOptions) {
      delegate.run(commandOptions);
   }

}
