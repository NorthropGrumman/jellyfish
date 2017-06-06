package com.ngc.seaside.bootstrap.impl.provider;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import com.ngc.seaside.bootstrap.IBootstrapCommandProvider;
import com.ngc.seaside.command.api.ICommandProvider;

/**
 * @author justan.provence@ngc.com
 */
public class BootstrapCommandProviderModule extends AbstractModule {

   @Override
   protected void configure() {
      // If you want to register multiple implementations of the same interface in Guice,
      // you need to use the multibinder scheme.
      Multibinder.newSetBinder(binder(), ICommandProvider.class)
               .addBinding()
               .to(BootstrapCommandProvider.class);

      Multibinder.newSetBinder(binder(), IBootstrapCommandProvider.class)
               .addBinding()
               .to(BootstrapCommandProvider.class);

   }

}
