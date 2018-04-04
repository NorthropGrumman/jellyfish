package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertestsconfig;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;

public class CreateJavaCucumberTestsConfigCommandGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IJellyFishCommand.class).addBinding().to(CreateJavaCucumberTestsConfigCommandGuiceWrapper.class);
      Multibinder.newSetBinder(binder(), ICommand.class).addBinding().to(CreateJavaCucumberTestsConfigCommandGuiceWrapper.class);
   }
}
