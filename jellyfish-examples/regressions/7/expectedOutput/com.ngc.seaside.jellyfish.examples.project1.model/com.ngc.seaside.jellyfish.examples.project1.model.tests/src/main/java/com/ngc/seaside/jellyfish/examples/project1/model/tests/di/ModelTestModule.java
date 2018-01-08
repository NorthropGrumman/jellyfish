package com.ngc.seaside.jellyfish.examples.project1.model.tests.di;

import com.google.inject.AbstractModule;

import com.ngc.seaside.jellyfish.examples.project1.model.tests.config.ModelTestConfiguration;

/**
 * This module configures Guice bindings for the Model steps.
 */
public class ModelTestModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(ModelTestConfiguration.class).asEagerSingleton();
   }
}