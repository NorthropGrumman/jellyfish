package com.ngc.seaside.jellyfish.cli.command.createdomain;

import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.JellyFishCommandConfiguration;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;

@JellyFishCommandConfiguration(autoTemplateProcessing = false)
public class CreateDomainCommandGuiceWrapper implements IJellyFishCommand {

   private final CreateDomainCommand delegate = new CreateDomainCommand();

   @Inject
   public CreateDomainCommandGuiceWrapper(ILogService logService,
                                          IPromptUserService promptService,
                                          ITemplateService templateService,
                                          IResourceService resourceService,
                                          IProjectNamingService projectNamingService) {
      delegate.setLogService(logService);
      delegate.setPromptService(promptService);
      delegate.setTemplateService(templateService);
      delegate.setResourceService(resourceService);
      delegate.setProjectNamingService(projectNamingService);
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
