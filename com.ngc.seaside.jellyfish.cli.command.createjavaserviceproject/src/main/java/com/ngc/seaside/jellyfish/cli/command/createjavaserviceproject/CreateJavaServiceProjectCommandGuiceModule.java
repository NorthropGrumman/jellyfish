package com.ngc.seaside.jellyfish.cli.command.createjavaserviceproject;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.ngc.seaside.command.api.ICommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;

public class CreateJavaServiceProjectCommandGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IJellyFishCommand.class).addBinding().to(CreateJavaServiceProjectCommandGuiceWrapper.class);
      Multibinder.newSetBinder(binder(), ICommand.class).addBinding().to(CreateJavaServiceProjectCommandGuiceWrapper.class);
   }
}
