package com.ngc.example.inhertance.databagsservice.tests.di;

import com.google.inject.AbstractModule;

import com.ngc.example.inhertance.databagsservice.tests.config.DataBagsServiceTestTransportConfiguration;

/**
 * This module configures Guice bindings for the DataBagsService steps.
 */
public class DataBagsServiceTestModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(DataBagsServiceTestTransportConfiguration.class).asEagerSingleton();
   }
}