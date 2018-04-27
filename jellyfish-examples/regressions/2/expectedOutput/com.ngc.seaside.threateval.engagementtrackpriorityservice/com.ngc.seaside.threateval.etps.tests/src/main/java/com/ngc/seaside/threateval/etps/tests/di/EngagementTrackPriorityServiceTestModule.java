package com.ngc.seaside.threateval.etps.tests.di;

import com.google.inject.AbstractModule;

import com.ngc.seaside.threateval.etps.testsconfig.EngagementTrackPriorityServiceTestTransportConfiguration;

/**
 * This module configures Guice bindings for the EngagementTrackPriorityService steps.
 */
public class EngagementTrackPriorityServiceTestModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(EngagementTrackPriorityServiceTestTransportConfiguration.class).asEagerSingleton();
   }
}