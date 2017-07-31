package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.JellyFishCommandConfiguration;
import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto.ITemplateDtoFactory;

@JellyFishCommandConfiguration(autoTemplateProcessing = false)
public class CreateJavaServiceBaseCommandGuiceWrapper implements IJellyFishCommand {

   private final CreateJavaServiceBaseCommand delegate = new CreateJavaServiceBaseCommand();

   @Inject
   public CreateJavaServiceBaseCommandGuiceWrapper(ILogService logService,
                                                   IPromptUserService promptUserService,
                                                   ITemplateService templateService,
                                                   @Named("java-service-base")
                                                   ITemplateDtoFactory templateDtoFactory) {
      delegate.setLogService(logService);
      delegate.setPromptService(promptUserService);
      delegate.setTemplateService(templateService);
      delegate.setTemplateDaoFactory(templateDtoFactory);
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
