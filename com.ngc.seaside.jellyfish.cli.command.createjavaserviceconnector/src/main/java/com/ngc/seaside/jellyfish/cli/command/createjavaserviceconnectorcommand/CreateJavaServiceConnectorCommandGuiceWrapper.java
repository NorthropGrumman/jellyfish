package com.ngc.seaside.jellyfish.cli.command.createjavaserviceconnectorcommand;

import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;

public class CreateJavaServiceConnectorCommandGuiceWrapper implements IJellyFishCommand {
   
   private final CreateJavaServiceConnectorCommand delegate = new CreateJavaServiceConnectorCommand();

   @Inject
   public CreateJavaServiceConnectorCommandGuiceWrapper(ILogService logService) {
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
