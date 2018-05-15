package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertests;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;

public class CreateJavaCucumberTestsCommandGuiceWrapper implements IJellyFishCommand {

   private final CreateJavaCucumberTestsCommand delegate = new CreateJavaCucumberTestsCommand();

   @Inject
   public CreateJavaCucumberTestsCommandGuiceWrapper(ILogService logService,
                                                     ITemplateService templateService,
                                                     IProjectNamingService projectService,
                                                     IPackageNamingService packageService,
                                                     IJavaServiceGenerationService generationService,
                                                     IBuildManagementService buildManagementService) {
      delegate.setLogService(logService);
      delegate.setTemplateService(templateService);
      delegate.setProjectNamingService(projectService);
      delegate.setPackageNamingService(packageService);
      delegate.setJavaServiceGenerationService(generationService);
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
