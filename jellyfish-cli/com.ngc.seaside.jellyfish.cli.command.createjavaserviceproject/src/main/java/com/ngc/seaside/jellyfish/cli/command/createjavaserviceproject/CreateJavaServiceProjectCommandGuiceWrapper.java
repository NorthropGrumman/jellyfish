package com.ngc.seaside.jellyfish.cli.command.createjavaserviceproject;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.jellyfish.api.JellyFishCommandConfiguration;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;

@JellyFishCommandConfiguration(autoTemplateProcessing = false)
public class CreateJavaServiceProjectCommandGuiceWrapper implements IJellyFishCommand {

   private final CreateJavaServiceProjectCommand delegate = new CreateJavaServiceProjectCommand();

   @Inject
   public CreateJavaServiceProjectCommandGuiceWrapper(ILogService logService,
                                                      IJellyFishCommandProvider jellyFishCommandProvider,
                                                      ITemplateService templateService,
                                                      IProjectNamingService projectNamingService) {
      delegate.setLogService(logService);
      delegate.setJellyFishCommandProvider(jellyFishCommandProvider);
      delegate.setTemplateService(templateService);
      delegate.setProjectNamingService(projectNamingService);
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
