package com.ngc.seaside.threateval.etps.tests.di;

import com.google.inject.AbstractModule;

import com.ngc.seaside.threateval.etps.tests.config.EngagementTrackPriorityServiceTestConfiguration;

/**
 * This module configures Guice bindings for the EngagementTrackPriorityService steps.
 */
public class EngagementTrackPriorityServiceTestModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(EngagementTrackPriorityServiceTestConfiguration.class).asEagerSingleton();
   }
}