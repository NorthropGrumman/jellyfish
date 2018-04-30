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



