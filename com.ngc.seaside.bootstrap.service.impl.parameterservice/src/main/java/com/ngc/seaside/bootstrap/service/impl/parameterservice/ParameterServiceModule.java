package com.ngc.seaside.bootstrap.service.impl.parameterservice;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.parameter.api.IParameterCollection;
import com.ngc.seaside.bootstrap.service.parameter.api.IParameterService;
import com.ngc.seaside.bootstrap.service.parameter.api.ParameterServiceException;
import com.ngc.seaside.bootstrap.service.property.api.IProperties;

import java.io.IOException;
import java.nio.file.Path;
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

   @Inject
   public void setLogService(ILogService ref) {
      delegate.setLogService(ref);
   }

   @Override
   protected void configure() {

   }
}

