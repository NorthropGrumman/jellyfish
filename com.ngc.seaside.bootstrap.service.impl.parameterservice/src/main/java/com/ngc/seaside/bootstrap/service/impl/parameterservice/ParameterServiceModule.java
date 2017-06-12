package com.ngc.seaside.bootstrap.service.impl.parameterservice;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.parameter.api.IParameterService;
import com.ngc.seaside.bootstrap.service.parameter.api.ParameterServiceException;
import com.ngc.seaside.command.api.IParameterCollection;

import java.util.List;

/**
 * The Guice module that wraps the ParameterService implementation.
 */
public class ParameterServiceModule extends AbstractModule implements IParameterService {

   private final ParameterService delegate = new ParameterService();

   @Override
   public IParameterCollection parseParameters(List<String> parameters) throws ParameterServiceException {
      return null;
   }

   @Override
   protected void configure() { bind(IParameterService.class).toInstance(this);  }

   @Inject
   public void setLogService(ILogService ref) {
      delegate.setLogService(ref);
   }
}



