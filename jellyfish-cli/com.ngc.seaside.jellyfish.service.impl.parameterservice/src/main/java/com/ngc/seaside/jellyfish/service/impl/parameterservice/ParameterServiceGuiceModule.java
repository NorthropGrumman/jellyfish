package com.ngc.seaside.jellyfish.service.impl.parameterservice;

import com.google.inject.AbstractModule;

import com.ngc.seaside.jellyfish.service.parameter.api.IParameterService;

/**
 * Configure the service for use in Guice
 */
public class ParameterServiceGuiceModule extends AbstractModule {
   @Override
   protected void configure() {
      bind(IParameterService.class).to(ParameterServiceGuiceWrapper.class);
   }
}



