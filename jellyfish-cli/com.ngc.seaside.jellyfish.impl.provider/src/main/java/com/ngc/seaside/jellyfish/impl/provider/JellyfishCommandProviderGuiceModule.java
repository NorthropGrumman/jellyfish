package com.ngc.seaside.jellyfish.impl.provider;

import com.google.inject.AbstractModule;

import com.ngc.seaside.jellyfish.api.ICommandProvider;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;

/**
 * Guice wrapper around the {@link JellyfishCommandProvider} and {@link DefaultCommandProvider} implementations.
 */
public class JellyfishCommandProviderGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(IJellyFishCommandProvider.class).to(JellyfishCommandProviderGuiceWrapper.class);
      bind(ICommandProvider.class).to(DefaultCommandProviderGuiceWrapper.class);
   }

}
