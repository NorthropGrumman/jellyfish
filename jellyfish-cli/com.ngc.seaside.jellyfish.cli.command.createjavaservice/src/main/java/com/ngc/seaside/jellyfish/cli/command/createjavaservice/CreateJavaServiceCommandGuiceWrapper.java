package com.ngc.seaside.jellyfish.cli.command.createjavaservice;

import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.JellyFishCommandConfiguration;
import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto.IServiceDtoFactory;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.IBaseServiceDtoFactory;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;

@JellyFishCommandConfiguration(autoTemplateProcessing = false)
public class CreateJavaServiceCommandGuiceWrapper implements IJellyFishCommand {

   private final CreateJavaServiceCommand delegate = new CreateJavaServiceCommand();

   @Inject
   public CreateJavaServiceCommandGuiceWrapper(ILogService logService,
                                               ITemplateService templateService,
                                               IServiceDtoFactory serviceTemplateDaoFactory,
                                               IBaseServiceDtoFactory serviceBaseTemplateDaoFactory,
                                               IProjectNamingService projectNamingService,
                                               IBuildManagementService buildManagementService) {
      delegate.setLogService(logService);
      delegate.setTemplateService(templateService);
      delegate.setServiceTemplateDaoFactory(serviceTemplateDaoFactory);
      delegate.setBaseServiceTemplateDaoFactory(serviceBaseTemplateDaoFactory);
      delegate.setProjectNamingService(projectNamingService);
      delegate.setBuildManagementService(buildManagementService);
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
