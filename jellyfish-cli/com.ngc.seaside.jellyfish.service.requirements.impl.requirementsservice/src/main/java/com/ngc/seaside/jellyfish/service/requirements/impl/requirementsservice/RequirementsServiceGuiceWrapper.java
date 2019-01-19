/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
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
