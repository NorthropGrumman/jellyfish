package com.ngc.seaside.jellyfish.cli.command.createjavaservicepubsubbridge;

import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.IBaseServiceDtoFactory;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;

public class CreateJavaServicePubsubBridgeCommandGuiceWrapper implements IJellyFishCommand {

   private final CreateJavaServicePubsubBridgeCommand delegate = new CreateJavaServicePubsubBridgeCommand();

   @Inject
   public CreateJavaServicePubsubBridgeCommandGuiceWrapper(ILogService logService,
                                                           IBuildManagementService buildManagementService,
                                                           ITemplateService templateService,
                                                           IBaseServiceDtoFactory templateDtoFactory,
                                                           IJavaServiceGenerationService generateService,
                                                           IPackageNamingService packageNamingService,
                                                           IProjectNamingService projectNamingService) {
      delegate.setLogService(logService);
      delegate.setBuildManagementService(buildManagementService);
      delegate.setTemplateService(templateService);
      delegate.setTemplateDaoFactory(templateDtoFactory);
      delegate.setGenerateService(generateService);
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
