package com.ngc.seaside.threateval.ctps.tests.di;

import com.google.inject.AbstractModule;

import com.ngc.seaside.threateval.ctps.tests.config.ClassificationTrackPriorityServiceTestConfiguration;

/**
 * This module configures Guice bindings for the ClassificationTrackPriorityService steps.
 */
public class ClassificationTrackPriorityServiceTestModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(ClassificationTrackPriorityServiceTestConfiguration.class).asEagerSingleton();
   }
}