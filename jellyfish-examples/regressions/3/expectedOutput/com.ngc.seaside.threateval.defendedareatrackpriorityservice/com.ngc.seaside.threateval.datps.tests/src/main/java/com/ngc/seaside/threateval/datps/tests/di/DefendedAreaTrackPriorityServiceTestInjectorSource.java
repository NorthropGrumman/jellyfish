package com.ngc.seaside.threateval.datps.tests.di;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;

import com.ngc.blocs.guice.module.LogServiceModule;
import com.ngc.blocs.guice.module.ResourceServiceModule;
import com.ngc.blocs.guice.module.ThreadServiceModule;
import com.ngc.seaside.threateval.datps.testsconfig.DefendedAreaTrackPriorityServiceTestConfigurationModule;

import cucumber.api.guice.CucumberModules;
import cucumber.runtime.java.guice.InjectorSource;

/**
 * This class handles the creation of the Guice injector.
 */
public class DefendedAreaTrackPriorityServiceTestInjectorSource implements InjectorSource {

   @Override
   public Injector getInjector() {
      return Guice.createInjector(Stage.PRODUCTION, CucumberModules.SCENARIO,
                                  new LogServiceModule(),
                                  new ResourceServiceModule(),
                                  new ThreadServiceModule(),
                                  new DefendedAreaTrackPriorityServiceTestConfigurationModule(),
                                  new DefendedAreaTrackPriorityServiceTestModule());
   }
}
