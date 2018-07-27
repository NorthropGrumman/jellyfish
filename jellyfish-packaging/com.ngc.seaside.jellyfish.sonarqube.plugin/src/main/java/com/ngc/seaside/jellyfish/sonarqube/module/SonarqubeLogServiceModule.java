package com.ngc.seaside.jellyfish.sonarqube.module;

import com.google.inject.AbstractModule;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.sonarqube.service.impl.SonarqubeLogService;

public class SonarqubeLogServiceModule extends AbstractModule {

   @Override
   protected void configure() {
      // Use the log service adapter that forwards to the SonarQube logging API.
      bind(ILogService.class).to(SonarqubeLogService.class);
   }
}
