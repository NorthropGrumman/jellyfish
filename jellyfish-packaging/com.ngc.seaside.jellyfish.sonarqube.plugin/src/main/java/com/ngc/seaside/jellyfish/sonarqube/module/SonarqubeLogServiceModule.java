package com.ngc.seaside.jellyfish.sonarqube.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.sonarqube.service.impl.SonarqubeLogService;

/**
 * A module for configuring the {@code SonarqubeLogService} with Guice.
 *
 * @see SonarqubeLogService
 */
public class SonarqubeLogServiceModule extends AbstractModule {

   @Override
   protected void configure() {
      // Use the log service adapter that forwards to the SonarQube logging API.
      bind(ILogService.class).to(SonarqubeLogService.class).in(Singleton.class);
   }
}
