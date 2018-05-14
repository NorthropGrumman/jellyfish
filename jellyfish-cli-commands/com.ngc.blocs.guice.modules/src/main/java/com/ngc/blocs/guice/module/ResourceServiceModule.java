package com.ngc.blocs.guice.module;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import com.ngc.blocs.service.resource.api.IResourceService;

/**
 *
 */
public class ResourceServiceModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(IResourceService.class).to(ResourceServiceDelegate.class).in(Scopes.SINGLETON);
   }
}
