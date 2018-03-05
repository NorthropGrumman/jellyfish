package com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.buildmgmt.impl.buildmgmtservice.config.DependenciesConfiguration;

public class BuildManagementServiceModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(IBuildManagementService.class).to(BuildManagementServiceGuiceWrapper.class).in(Singleton.class);
      bind(DependenciesConfiguration.class).toInstance(DefaultDependenciesConfiguration.getConfig());
   }
}
