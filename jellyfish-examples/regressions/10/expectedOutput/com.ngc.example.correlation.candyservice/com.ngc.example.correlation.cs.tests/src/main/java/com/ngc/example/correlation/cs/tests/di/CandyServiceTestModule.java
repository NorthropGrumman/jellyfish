package com.ngc.example.correlation.cs.tests.di;

import com.google.inject.AbstractModule;

import com.ngc.example.correlation.cs.tests.config.CandyServiceTestConfiguration;

/**
 * This module configures Guice bindings for the CandyService steps.
 */
public class CandyServiceTestModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(CandyServiceTestConfiguration.class).asEagerSingleton();
   }
}