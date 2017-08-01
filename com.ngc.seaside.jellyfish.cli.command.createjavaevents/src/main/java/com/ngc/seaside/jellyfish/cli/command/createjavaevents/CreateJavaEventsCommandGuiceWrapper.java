package com.ngc.seaside.jellyfish.cli.command.createjavaevents;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.jellyfish.api.JellyFishCommandConfiguration;

@JellyFishCommandConfiguration(autoTemplateProcessing = false)
public class CreateJavaEventsCommandGuiceWrapper implements IJellyFishCommand {

   private final CreateJavaEventsCommand delegate = new CreateJavaEventsCommand();

   @Inject
   public CreateJavaEventsCommandGuiceWrapper(ILogService logService,
                                              IResourceService resourceService,
                                              IPromptUserService promptUserService,
                                              IJellyFishCommandProvider jellyFishCommandProvider) {
      delegate.setLogService(logService);
      delegate.setResourceService(resourceService);
      delegate.setPromptUserService(promptUserService);
      delegate.setJellyFishCommandProvider(jellyFishCommandProvider);
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
