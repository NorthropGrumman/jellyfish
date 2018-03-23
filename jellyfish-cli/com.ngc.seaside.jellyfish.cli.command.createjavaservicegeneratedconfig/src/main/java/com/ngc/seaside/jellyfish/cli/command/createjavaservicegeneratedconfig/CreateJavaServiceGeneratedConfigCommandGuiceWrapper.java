package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig;

import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;

public class CreateJavaServiceGeneratedConfigCommandGuiceWrapper implements IJellyFishCommand {

   private final CreateJavaServiceGeneratedConfigCommand delegate = new CreateJavaServiceGeneratedConfigCommand();

   @Inject
   public CreateJavaServiceGeneratedConfigCommandGuiceWrapper(ILogService logService) {
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
   public void run(IJellyFishCommandOptions commandOptions) {
      delegate.run(commandOptions);
   }

}
