package com.ngc.seaside.jellyfish.cli.command.createjavadistribution;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.api.JellyFishCommandConfiguration;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;

@JellyFishCommandConfiguration(autoTemplateProcessing = false)
public class CreateJavaDistributionCommandGuiceWrapper implements IJellyFishCommand {

   private final CreateJavaDistributionCommand delegate = new CreateJavaDistributionCommand();

   @Inject
   public CreateJavaDistributionCommandGuiceWrapper(ILogService logService,
                                                    ITemplateService templateService,
                                                    IProjectNamingService projectNamingService,
                                                    IPackageNamingService packageNamingService,
                                                    IBuildManagementService buildManagementService,
                                                    ITransportConfigurationService transportConfigurationService) {
      delegate.setLogService(logService);
      delegate.setTemplateService(templateService);
      delegate.setProjectNamingService(projectNamingService);
      delegate.setPackageNamingService(packageNamingService);
      delegate.setBuildManagementService(buildManagementService);
      delegate.setTransportConfigurationService(transportConfigurationService);
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
