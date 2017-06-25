package com.ngc.seaside.bootstrap.service.impl.parameterservice;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.parameter.api.IParameterService;
import com.ngc.seaside.bootstrap.service.parameter.api.ParameterServiceException;
import com.ngc.seaside.command.api.IParameterCollection;
import com.ngc.seaside.command.api.IUsage;

import java.util.List;

/**
 * The Guice module that wraps the ParameterService implementation.
 */
public class ParameterServiceModule extends AbstractModule implements IParameterService {

   private final ParameterService delegate = new ParameterService();

   protected void configure() {
      bind(IParameterService.class).toInstance(this);
   }

   @Override
   public IParameterCollection parseParameters(List<String> parameters)
         throws ParameterServiceException {
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

   @Inject
   public void setLogService(ILogService ref) {
      delegate.setLogService(ref);
   }
}



