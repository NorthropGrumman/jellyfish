package com.ngc.seaside.bootstrap.service.impl.repositoryservice;

import com.google.inject.AbstractModule;
import com.ngc.seaside.bootstrap.service.repository.api.IRepositoryService;

/**
 * Configure the service for use in Guice
 */
public class RepositoryServiceGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(IRepositoryService.class).to(RepositoryServiceGuiceWrapper.class);
   }


}
