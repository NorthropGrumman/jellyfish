package com.ngc.seaside.jellyfish.cli.command.createprotocolbuffermessages;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.jellyfish.api.JellyFishCommandConfiguration;

/**
 * Wrap the service using Guice Injection
 */
@JellyFishCommandConfiguration(autoTemplateProcessing = false)
public class CreateProtocolbufferMessagesCommandGuiceWrapper implements IJellyFishCommand {

   private final CreateProtocolbufferMessagesCommand delegate = new CreateProtocolbufferMessagesCommand();

   @Inject
   public CreateProtocolbufferMessagesCommandGuiceWrapper(ILogService logService,
                                                          IJellyFishCommandProvider jellyfishCommandProvider,
                                                          IResourceService resourceService,
                                                          IJellyFishCommandProvider provider) {
      delegate.setLogService(logService);
      delegate.setJellyFishCommandProvider(jellyfishCommandProvider);
      delegate.setResourceService(resourceService);
      delegate.setJellyFishCommandProvider(provider);
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
