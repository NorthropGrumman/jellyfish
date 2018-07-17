package com.ngc.seaside.systemdescriptor.service.impl.m2repositoryservice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.service.repository.api.IRepositoryService;

/**
 * Configure the service for use in Guice
 */
public class RepositoryServiceGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(IRepositoryService.class).to(RepositoryServiceGuiceWrapper.class);
   }

   /**
    * Guice wrapper for {@link GradlePropertiesService}.
    */
   @Provides
   public GradlePropertiesService getGradlePropertiesService(ILogService ref) {
      GradlePropertiesService service = new GradlePropertiesService();
      service.setLogService(ref);
      service.activate();
      return service;
   }
}
