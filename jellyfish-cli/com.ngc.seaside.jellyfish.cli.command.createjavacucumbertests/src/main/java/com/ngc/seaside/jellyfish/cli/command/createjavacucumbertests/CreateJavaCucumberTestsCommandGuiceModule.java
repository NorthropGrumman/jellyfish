package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertests;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;

public class CreateJavaCucumberTestsCommandGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IJellyFishCommand.class).addBinding()
            .to(CreateJavaCucumberTestsCommandGuiceWrapper.class);
      Multibinder.newSetBinder(binder(), ICommand.class).addBinding()
            .to(CreateJavaCucumberTestsCommandGuiceWrapper.class);
   }
}
