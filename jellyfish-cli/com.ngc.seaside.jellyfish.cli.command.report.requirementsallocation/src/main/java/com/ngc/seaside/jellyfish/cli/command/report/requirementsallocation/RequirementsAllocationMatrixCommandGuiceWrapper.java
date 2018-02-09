package com.ngc.seaside.jellyfish.cli.command.report.requirementsallocation;

import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.JellyFishCommandConfiguration;
import com.ngc.seaside.jellyfish.service.requirements.api.IRequirementsService;

@JellyFishCommandConfiguration(autoTemplateProcessing = false)
public class RequirementsAllocationMatrixCommandGuiceWrapper implements IJellyFishCommand {

   private final RequirementsAllocationMatrixCommand delegate = new RequirementsAllocationMatrixCommand();

   @Inject
   public RequirementsAllocationMatrixCommandGuiceWrapper(ILogService logService,
                                                          IRequirementsService requirementsService) {
      delegate.setLogService(logService);
      delegate.setRequirementsService(requirementsService);
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
