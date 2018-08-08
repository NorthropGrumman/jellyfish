package com.ngc.seaside.threateval.te.tests.di;

import com.google.inject.AbstractModule;

import com.ngc.seaside.threateval.te.testsconfig.ThreatEvaluationTestTransportConfiguration;

/**
 * This module configures Guice bindings for the ThreatEvaluation steps.
 */
public class ThreatEvaluationTestModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(ThreatEvaluationTestTransportConfiguration.class).asEagerSingleton();
   }
}