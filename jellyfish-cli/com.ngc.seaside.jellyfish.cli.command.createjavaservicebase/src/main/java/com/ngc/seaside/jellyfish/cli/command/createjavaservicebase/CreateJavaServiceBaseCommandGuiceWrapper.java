package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase;

import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.JellyFishCommandConfiguration;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.IBaseServiceDtoFactory;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;

@JellyFishCommandConfiguration(autoTemplateProcessing = false)
public class CreateJavaServiceBaseCommandGuiceWrapper implements IJellyFishCommand {

   private final CreateJavaServiceBaseCommand delegate = new CreateJavaServiceBaseCommand();

   @Inject
   public CreateJavaServiceBaseCommandGuiceWrapper(ILogService logService,
                                                   ITemplateService templateService,
                                                   IBaseServiceDtoFactory templateDtoFactory,
                                                   IProjectNamingService projectNamingService,
                                                   IBuildManagementService buildManagementService) {
      delegate.setLogService(logService);
      delegate.setTemplateService(templateService);
      delegate.setTemplateDaoFactory(templateDtoFactory);
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
