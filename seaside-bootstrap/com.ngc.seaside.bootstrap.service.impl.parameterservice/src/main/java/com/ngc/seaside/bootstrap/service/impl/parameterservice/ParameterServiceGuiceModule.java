package com.ngc.seaside.bootstrap.service.impl.parameterservice;

import com.google.inject.AbstractModule;

import com.ngc.seaside.bootstrap.service.parameter.api.IParameterService;

/**
 * Configure the service for use in Guice
 */
public class ParameterServiceGuiceModule extends AbstractModule {
   @Override
   protected void configure() {
      bind(IParameterService.class).to(ParameterServiceGuiceWrapper.class);
   }
}



