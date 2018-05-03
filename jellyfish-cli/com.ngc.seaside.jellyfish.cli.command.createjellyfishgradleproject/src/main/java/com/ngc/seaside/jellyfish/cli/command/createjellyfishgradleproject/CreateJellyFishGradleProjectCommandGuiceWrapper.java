package com.ngc.seaside.jellyfish.cli.command.createjellyfishgradleproject;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;

public class CreateJellyFishGradleProjectCommandGuiceWrapper implements IJellyFishCommand {

   private final CreateJellyFishGradleProjectCommand delegate = new CreateJellyFishGradleProjectCommand();

   @Inject
   public CreateJellyFishGradleProjectCommandGuiceWrapper(ILogService logService,
                                                          ITemplateService templateService,
                                                          IBuildManagementService buildManagementService) {
      delegate.setLogService(logService);
      delegate.setTemplateService(templateService);
      delegate.setBuildManagementService(buildManagementService);
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
