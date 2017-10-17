package com.ngc.test.model1.tests.di;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;

import com.ngc.blocs.guice.module.LogServiceModule;
import com.ngc.blocs.guice.module.ResourceServiceModule;
import com.ngc.blocs.guice.module.ThreadServiceModule;
import com.ngc.seaside.service.transport.impl.defaultz.module.DefaultTransportServiceModule;

import cucumber.api.guice.CucumberModules;
import cucumber.runtime.java.guice.InjectorSource;

/**
 * This class handles the creation of the Guice injector.
 */
public class Model1TestInjectorSource implements InjectorSource {

   @Override
   public Injector getInjector() {
      return Guice.createInjector(Stage.PRODUCTION, CucumberModules.SCENARIO,
                                  new LogServiceModule(),
                                  new ResourceServiceModule(),
                                  new ThreadServiceModule(),
                                  new DefaultTransportServiceModule(),
                                  new Model1TestModule());
   }
}
