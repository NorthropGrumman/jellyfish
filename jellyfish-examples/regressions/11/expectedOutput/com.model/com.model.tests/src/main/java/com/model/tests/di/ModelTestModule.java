package com.model.tests.di;

import com.google.inject.AbstractModule;

import com.model.tests.config.ModelTestTransportConfiguration;

/**
 * This module configures Guice bindings for the Model steps.
 */
public class ModelTestModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(ModelTestTransportConfiguration.class).asEagerSingleton();
   }
}