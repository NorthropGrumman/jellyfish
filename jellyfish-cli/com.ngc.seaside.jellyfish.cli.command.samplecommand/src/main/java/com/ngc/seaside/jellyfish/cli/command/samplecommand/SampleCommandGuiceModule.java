package com.ngc.seaside.jellyfish.cli.command.samplecommand;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;

/**
 * Configure the service for use in Guice
 */
public class SampleCommandGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IJellyFishCommand.class)
               .addBinding().to(SampleCommandGuiceWrapper.class);
      Multibinder.newSetBinder(binder(), ICommand.class)
               .addBinding().to(SampleCommandGuiceWrapper.class);
   }

}
