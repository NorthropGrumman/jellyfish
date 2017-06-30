package com.ngc.seaside.bootstrap.impl.provider;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import com.ngc.seaside.bootstrap.api.IBootstrapCommandProvider;

/**
 * Guice wrapper around the {@link BootstrapCommandProvider} implementation.
 */
public class BootstrapCommandProviderGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(IBootstrapCommandProvider.class).to(BootstrapCommandProviderGuiceWrapper.class);
   }

}
