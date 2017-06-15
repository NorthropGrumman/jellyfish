package com.ngc.seaside.bootstrap.service.impl.parameterservice;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.parameter.api.IParameterService;
import com.ngc.seaside.bootstrap.service.parameter.api.ParameterServiceException;
import com.ngc.seaside.command.api.IParameterCollection;
import com.ngc.seaside.command.api.IUsage;

import java.util.List;
import java.util.Set;

/**
 * The Guice module that wraps the ParameterService implementation.
 */
public class ParameterServiceModule extends AbstractModule implements IParameterService {

   private final ParameterService delegate = new ParameterService();

   @Override
   public IParameterCollection parseParameters(IUsage usage, List<String> parameters) throws ParameterServiceException {
      return delegate.parseParameters(usage, parameters);
   }

   @Override
   public Set<String> getRequiredParameters() {
      return delegate.getRequiredParameters();
   }

   @Override
   public void setRequiredParameters(Set<String> newRequiredParameters) {
      delegate.setRequiredParameters(newRequiredParameters);
   }

   @Override
   protected void configure() {
      bind(IParameterService.class).toInstance(this);
   }
}



