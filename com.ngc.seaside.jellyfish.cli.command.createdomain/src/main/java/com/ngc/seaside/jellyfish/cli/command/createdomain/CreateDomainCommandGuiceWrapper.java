package com.ngc.seaside.jellyfish.cli.command.createdomain;

import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.JellyFishCommandConfiguration;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;

@JellyFishCommandConfiguration(autoTemplateProcessing = false)
public class CreateDomainCommandGuiceWrapper implements IJellyFishCommand {

   private final CreateDomainCommand delegate = new CreateDomainCommand();

   @Inject
   public CreateDomainCommandGuiceWrapper(ILogService logService,
                                          ITemplateService templateService,
                                          IResourceService resourceService,
                                          IProjectNamingService projectNamingService,
                                          IPackageNamingService packageNamingService) {
      delegate.setLogService(logService);
      delegate.setTemplateService(templateService);
      delegate.setResourceService(resourceService);
      delegate.setProjectNamingService(projectNamingService);
      delegate.setPackageNamingService(packageNamingService);
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
