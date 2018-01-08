package com.ngc.seaside.jellyfish.cli.command.samplecommand;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;

/**
 * Wrap the service using Guice Injection
 */
@Singleton
public class SampleCommandGuiceWrapper implements IJellyFishCommand {

   private final SampleCommand delegate = new SampleCommand();

   @Inject
   public SampleCommandGuiceWrapper(ILogService logService) {
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
