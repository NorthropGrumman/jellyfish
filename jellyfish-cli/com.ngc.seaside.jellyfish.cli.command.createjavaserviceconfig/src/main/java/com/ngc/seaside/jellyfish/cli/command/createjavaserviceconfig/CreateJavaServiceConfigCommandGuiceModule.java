package com.ngc.seaside.jellyfish.cli.command.createjavaserviceconfig;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;

public class CreateJavaServiceConfigCommandGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IJellyFishCommand.class).addBinding()
            .to(CreateJavaServiceConfigCommandGuiceWrapper.class);
      Multibinder.newSetBinder(binder(), ICommand.class).addBinding()
            .to(CreateJavaServiceConfigCommandGuiceWrapper.class);
   }
}
