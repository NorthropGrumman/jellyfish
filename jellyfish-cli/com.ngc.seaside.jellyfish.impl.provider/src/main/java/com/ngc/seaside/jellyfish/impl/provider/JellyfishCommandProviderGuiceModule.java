package com.ngc.seaside.jellyfish.impl.provider;

import com.google.inject.AbstractModule;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;

/**
 * Guice wrapper around the {@link JellyfishCommandProvider} implementation.
 */
public class JellyfishCommandProviderGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(IJellyFishCommandProvider.class).to(JellyfishCommandProviderGuiceWrapper.class);
   }

}
