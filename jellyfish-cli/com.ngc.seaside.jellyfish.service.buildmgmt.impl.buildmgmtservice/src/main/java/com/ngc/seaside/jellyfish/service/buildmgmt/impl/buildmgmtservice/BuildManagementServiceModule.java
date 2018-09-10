/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
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
