/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish.service.impl.parameterservice;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.parameter.api.IParameterService;
import com.ngc.seaside.jellyfish.service.parameter.api.ParameterServiceException;

import java.util.List;
import java.util.Map;

/**
 * Wrap the service using Guice Injection
 */
@Singleton
public class ParameterServiceGuiceWrapper implements IParameterService {

   private final ParameterService delegate = new ParameterService();

   @Inject
   public ParameterServiceGuiceWrapper(ILogService logService) {
      delegate.setLogService(logService);
      delegate.activate();
   }

   @Override
   public IParameterCollection parseParameters(List<String> parameters)
         throws ParameterServiceException {
      return delegate.parseParameters(parameters);
   }

   @Override
   public IParameterCollection parseParameters(Map<String, ?> parameters) {
      return delegate.parseParameters(parameters);
   }

   @Override
   public boolean isUsageSatisfied(IUsage usage, IParameterCollection collection) {
      return delegate.isUsageSatisfied(usage, collection);
   }

   @Override
   public IParameterCollection getUnsetRequiredParameters(IUsage usage,
                                                          IParameterCollection collection) {
      return delegate.getUnsetRequiredParameters(usage, collection);
   }

}



