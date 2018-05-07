package com.ngc.seaside.jellyfish.cli.command.createjavaevents;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import com.ngc.seaside.jellyfish.api.IJellyFishCommand;

public class CreateJavaEventsCommandGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IJellyFishCommand.class)
            .addBinding()
            .to(CreateJavaEventsCommandGuiceWrapper.class);
   }
}
