package com.ngc.seaside.jellyfish.cli.command.createjellyfishcommand;

import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.JellyFishCommandConfiguration;

@JellyFishCommandConfiguration(autoTemplateProcessing = false, requireValidSystemDescriptor = false)
public class CreateJellyFishCommandCommandGuiceWrapper implements IJellyFishCommand {

   private final CreateJellyFishCommandCommand delegate = new CreateJellyFishCommandCommand();

   @Inject
   public CreateJellyFishCommandCommandGuiceWrapper(ILogService logService, ITemplateService templateService) {
      delegate.setLogService(logService);
      delegate.setTemplateService(templateService);
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
