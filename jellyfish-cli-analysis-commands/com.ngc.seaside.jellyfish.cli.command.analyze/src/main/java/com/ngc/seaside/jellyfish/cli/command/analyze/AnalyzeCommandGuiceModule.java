package com.ngc.seaside.jellyfish.cli.command.analyze;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import com.ngc.seaside.jellyfish.api.IJellyFishCommand;

/**
 * The Guice module for the analyze command.
 */
public class AnalyzeCommandGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IJellyFishCommand.class)
            .addBinding()
            .to(AnalyzeCommandGuiceWrapper.class);
   }
}
