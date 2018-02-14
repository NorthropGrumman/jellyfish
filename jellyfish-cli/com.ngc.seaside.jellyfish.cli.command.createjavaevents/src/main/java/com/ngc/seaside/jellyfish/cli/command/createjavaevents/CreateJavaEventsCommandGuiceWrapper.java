package com.ngc.seaside.jellyfish.cli.command.createjavaevents;

import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.JellyFishCommandConfiguration;
import com.ngc.seaside.jellyfish.service.codegen.api.IDataFieldGenerationService;
import com.ngc.seaside.jellyfish.service.data.api.IDataService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;

@JellyFishCommandConfiguration(autoTemplateProcessing = false)
public class CreateJavaEventsCommandGuiceWrapper implements IJellyFishCommand {

   private final CreateJavaEventsCommand delegate = new CreateJavaEventsCommand();

   @Inject
   public CreateJavaEventsCommandGuiceWrapper(ILogService logService,
                                              IProjectNamingService projectNamingService,
                                              IPackageNamingService packageNamingService,
                                              ITemplateService templateService,
                                              IDataService dataService,
                                              IDataFieldGenerationService dataFieldGenerationService) {
      delegate.setLogService(logService);
      delegate.setProjectNamingService(projectNamingService);
      delegate.setPackageNamingService(packageNamingService);
      delegate.setTemplateService(templateService);
      delegate.setDataService(dataService);
      delegate.setDataFieldGenerationService(dataFieldGenerationService);
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