package com.ngc.seaside.threateval.datps.tests.di;

import com.google.inject.AbstractModule;

import com.ngc.seaside.threateval.datps.tests.config.DefendedAreaTrackPriorityServiceTestConfiguration;

/**
 * This module configures Guice bindings for the DefendedAreaTrackPriorityService steps.
 */
public class DefendedAreaTrackPriorityServiceTestModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(DefendedAreaTrackPriorityServiceTestConfiguration.class).asEagerSingleton();
   }
}