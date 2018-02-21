package com.ngc.example.models.modelservice.tests.di;

import com.google.inject.AbstractModule;

import com.ngc.example.models.modelservice.tests.config.ModelServiceTestConfiguration;

/**
 * This module configures Guice bindings for the ModelService steps.
 */
public class ModelServiceTestModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(ModelServiceTestConfiguration.class).asEagerSingleton();
   }
}