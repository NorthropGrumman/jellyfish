package com.ngc.seaside.jellyfish.cli.command.createprotocolbuffermessages;

import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createdomain.CreateDomainCommandGuiceWrapper;

public class CreateProtocolbufferMessagesCommandGuiceWrapper implements IJellyFishCommand {
   
   private final CreateProtocolbufferMessagesCommand delegate = new CreateProtocolbufferMessagesCommand();

   @Inject
   public CreateProtocolbufferMessagesCommandGuiceWrapper(ILogService logService) {
      delegate.setLogService(logService);
   }
   
   @Inject
   public void setDomainCommand(CreateDomainCommandGuiceWrapper command) {
      delegate.setDomainCommand(command);
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
