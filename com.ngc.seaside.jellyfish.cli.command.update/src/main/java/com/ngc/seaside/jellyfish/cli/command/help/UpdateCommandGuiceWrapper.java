package com.ngc.seaside.jellyfish.cli.command.help;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.JellyFishCommandConfiguration;

import java.util.Set;

@Singleton
@JellyFishCommandConfiguration(requireValidSystemDescriptor = false)
public class UpdateCommandGuiceWrapper implements IJellyFishCommand {

   private final UpdateCommand delegate = new UpdateCommand();

   @Inject
   public UpdateCommandGuiceWrapper(ILogService logService) {
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
   public void run(IJellyFishCommandOptions commandOptions) {
      delegate.run(commandOptions);
   }
   
}
