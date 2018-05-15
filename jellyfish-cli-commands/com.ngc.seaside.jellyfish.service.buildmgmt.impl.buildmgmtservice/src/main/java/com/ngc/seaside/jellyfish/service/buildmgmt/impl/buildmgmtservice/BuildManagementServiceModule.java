package com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice.config.DependenciesConfiguration;

public class BuildManagementServiceModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(IBuildManagementService.class).to(BuildManagementServiceGuiceWrapper.class).in(Singleton.class);
      // Register the config with Guice.  It is possible to override this configuration my registering a different
      // instance of DependenciesConfiguration.
      bind(DependenciesConfiguration.class).toInstance(DefaultDependenciesConfiguration.getConfig());
   }
}
