package com.ngc.seaside.jellyfish.sonarqube.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.sonarqube.service.impl.NoOpLogService;

/**
 * A module for configuring the {@code NoOpLogServiceModule} with Guice.
 *
 * @see NoOpLogService
 */
public class NoOpLogServiceModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(ILogService.class).to(NoOpLogService.class).in(Singleton.class);
   }
}
