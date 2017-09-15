package com.ngc.seaside.jellyfish.cli.command.createjavaservice;

import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.JellyFishCommandConfiguration;
import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto.IServiceDtoFactory;

@JellyFishCommandConfiguration(autoTemplateProcessing = false)
public class CreateJavaServiceCommandGuiceWrapper implements IJellyFishCommand {

   private final CreateJavaServiceCommand delegate = new CreateJavaServiceCommand();

   @Inject
   public CreateJavaServiceCommandGuiceWrapper(ILogService logService,
                                               IPromptUserService promptUserService,
                                               ITemplateService templateService,
                                               IServiceDtoFactory templateDaoFactory) {
      delegate.setLogService(logService);
      delegate.setPromptService(promptUserService);
      delegate.setTemplateService(templateService);
      delegate.setTemplateDaoFactory(templateDaoFactory);
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
