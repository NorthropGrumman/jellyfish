package com.ngc.seaside.systemdescriptor.service.impl.m2repositoryservice;

import com.google.inject.AbstractModule;
import com.ngc.seaside.systemdescriptor.service.api.IRepositoryService;

/**
 * Configure the service for use in Guice
 */
public class RepositoryServiceGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(IRepositoryService.class).to(RepositoryServiceGuiceWrapper.class);
   }


}
