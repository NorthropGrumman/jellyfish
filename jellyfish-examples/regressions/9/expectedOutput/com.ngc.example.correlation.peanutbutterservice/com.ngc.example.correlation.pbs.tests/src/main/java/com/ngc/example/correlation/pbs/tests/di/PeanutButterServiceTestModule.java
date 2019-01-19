
package com.ngc.example.correlation.pbs.tests.di;

import com.google.inject.AbstractModule;

import com.ngc.example.correlation.pbs.tests.config.PeanutButterServiceTestTransportConfiguration;

/**
 * This module configures Guice bindings for the PeanutButterService steps.
 */
public class PeanutButterServiceTestModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(PeanutButterServiceTestTransportConfiguration.class).asEagerSingleton();
   }
}
