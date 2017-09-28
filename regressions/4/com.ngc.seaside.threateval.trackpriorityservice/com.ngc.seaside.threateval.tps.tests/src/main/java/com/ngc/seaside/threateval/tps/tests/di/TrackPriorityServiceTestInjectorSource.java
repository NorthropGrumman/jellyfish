package com.ngc.seaside.threateval.tps.tests.di;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;

import com.ngc.blocs.guice.module.LogServiceModule;
import com.ngc.blocs.guice.module.ResourceServiceModule;
import com.ngc.blocs.guice.module.ThreadServiceModule;
import com.ngc.seaside.service.transport.impl.defaultz.module.DefaultTransportServiceModule;
import com.ngc.seaside.service.transport.impl.provider.multicast.module.MulticastTransportProviderModule;

import cucumber.api.guice.CucumberModules;
import cucumber.runtime.java.guice.InjectorSource;

/**
 * This class handles the creation of the Guice injector.
 */
public class TrackPriorityServiceTestInjectorSource implements InjectorSource {

   @Override
   public Injector getInjector() {
      return Guice.createInjector(Stage.PRODUCTION, CucumberModules.SCENARIO,
                                  new LogServiceModule(),
                                  new ResourceServiceModule(),
                                  new ThreadServiceModule(),
                                  new DefaultTransportServiceModule(),
                                  new TrackPriorityServiceTestModule());
   }
}
