package com.ngc.seaside.bootstrap.command.impl.help;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import com.ngc.seaside.bootstrap.api.IBootstrapCommand;
import com.ngc.seaside.command.api.ICommand;

/**
 * Configure the service for use in Guice
 */
public class HelpCommandGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IBootstrapCommand.class)
               .addBinding().to(HelpCommandGuiceWrapper.class);
      Multibinder.newSetBinder(binder(), ICommand.class)
               .addBinding().to(HelpCommandGuiceWrapper.class);
   }
}
