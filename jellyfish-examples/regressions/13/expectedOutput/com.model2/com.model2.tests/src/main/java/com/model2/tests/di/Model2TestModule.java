package com.model2.tests.di;

import com.google.inject.AbstractModule;

import com.model2.tests.config.Model2TestTransportConfiguration;

/**
 * This module configures Guice bindings for the Model2 steps.
 */
public class Model2TestModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(Model2TestTransportConfiguration.class).asEagerSingleton();
   }
}