package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.IBaseServiceDtoFactory;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;

public class CreateJavaServiceGeneratedConfigCommandGuiceWrapper implements IJellyFishCommand {

   private final CreateJavaServiceGeneratedConfigCommand delegate = new CreateJavaServiceGeneratedConfigCommand();

   @Inject
   public CreateJavaServiceGeneratedConfigCommandGuiceWrapper(ILogService logService,
                                                              ITemplateService templateService,
                                                              IBaseServiceDtoFactory baseServiceDtoFactory,
                                                              IProjectNamingService projectNamingService,
                                                              IPackageNamingService packageNamingService,
                                                              IBuildManagementService buildManagementService,
                                                              ITransportConfigurationService transportConfigService,
                                                              IScenarioService scenarioService,
                                                              IJavaServiceGenerationService generateService) {
      delegate.setLogService(logService);
      delegate.setTemplateService(templateService);
      delegate.setBaseServiceDtoFactory(baseServiceDtoFactory);
      delegate.setProjectNamingService(projectNamingService);
      delegate.setPackageNamingService(packageNamingService);
      delegate.setBuildManagementService(buildManagementService);
      delegate.setTransportConfigurationService(transportConfigService);
      delegate.setScenarioService(scenarioService);
      delegate.setJavaServiceGenerationService(generateService);
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
