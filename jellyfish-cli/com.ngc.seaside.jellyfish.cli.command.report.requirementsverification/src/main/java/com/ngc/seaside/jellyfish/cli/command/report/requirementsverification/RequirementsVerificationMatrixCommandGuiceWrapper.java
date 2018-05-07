package com.ngc.seaside.jellyfish.cli.command.report.requirementsverification;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.feature.api.IFeatureService;
import com.ngc.seaside.jellyfish.service.requirements.api.IRequirementsService;

public class RequirementsVerificationMatrixCommandGuiceWrapper implements IJellyFishCommand {

   private final RequirementsVerificationMatrixCommand delegate = new RequirementsVerificationMatrixCommand();

   @Inject
   public RequirementsVerificationMatrixCommandGuiceWrapper(ILogService logService, 
                                                            IFeatureService featureService,
                                                            IRequirementsService requirementsService) {
      delegate.setLogService(logService);
      delegate.setFeatureService(featureService);
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
