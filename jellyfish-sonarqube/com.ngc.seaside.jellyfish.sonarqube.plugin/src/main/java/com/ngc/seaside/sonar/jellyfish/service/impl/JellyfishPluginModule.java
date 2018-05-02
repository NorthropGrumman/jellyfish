package com.ngc.seaside.sonar.jellyfish.service.impl;

import com.google.inject.AbstractModule;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.service.impl.m2repositoryservice.RepositoryServiceGuiceModule;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;

/**
 * A Guava module that registers components so the plugin can use the
 * {@link com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService}.
 */
public class JellyfishPluginModule extends AbstractModule {

   @Override
   protected void configure() {
      // Use the log service adapter that forwards to the SonarQube logging API.
      bind(ILogService.class).to(SonarqubeLogService.class);
      // Include the module for the XText service impl.
      install(XTextSystemDescriptorServiceModule.forStandaloneUsage());
      // Include the module for the repository service.
      install(new RepositoryServiceGuiceModule());
   }
}
