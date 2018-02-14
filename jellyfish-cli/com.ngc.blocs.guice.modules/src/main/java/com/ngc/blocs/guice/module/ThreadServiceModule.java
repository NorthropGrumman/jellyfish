package com.ngc.blocs.guice.module;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import com.ngc.blocs.service.thread.api.IThreadService;

public class ThreadServiceModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(IThreadService.class).to(ThreadServiceDelegate.class).in(Scopes.SINGLETON);
   }
}
