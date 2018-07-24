package com.ngc.seaside.jellyfish.service.requirements.impl.requirementsservice;

import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.requirements.api.IRequirementsService;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;

import java.util.Set;

public class RequirementsServiceGuiceWrapper implements IRequirementsService {

   private final RequirementsService delegate;
   
   @Inject
   public RequirementsServiceGuiceWrapper(ILogService logService) {
      delegate = new RequirementsService();
      delegate.setLogService(logService);
   }
   
   @Override
   public Set<String> getRequirements(IJellyFishCommandOptions options, IMetadata metadata) {
      return delegate.getRequirements(options, metadata);
   }

}
