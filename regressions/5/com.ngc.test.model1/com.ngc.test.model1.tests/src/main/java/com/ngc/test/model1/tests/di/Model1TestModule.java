package com.ngc.test.model1.tests.di;

import com.google.inject.AbstractModule;

import com.ngc.test.model1.tests.config.Model1TestConfiguration;

/**
 * This module configures Guice bindings for the Model1 steps.
 */
public class Model1TestModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(Model1TestConfiguration.class).asEagerSingleton();
   }
}