
package com.ngc.seaside.threateval.tps.tests.di;

import com.google.inject.AbstractModule;

import com.ngc.seaside.threateval.tps.testsconfig.TrackPriorityServiceTestTransportConfiguration;

/**
 * This module configures Guice bindings for the TrackPriorityService steps.
 */
public class TrackPriorityServiceTestModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(TrackPriorityServiceTestTransportConfiguration.class).asEagerSingleton();
   }
}
