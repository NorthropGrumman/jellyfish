package test.model.tests.di;

import com.google.inject.AbstractModule;

import test.model.tests.config.ModelTestConfiguration;

/**
 * This module configures Guice bindings for the Model steps.
 */
public class ModelTestModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(ModelTestConfiguration.class).asEagerSingleton();
   }
}