package com.ngc.seaside.jellyfish.cli.command.createjellyfishcommand;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.ngc.seaside.command.api.ICommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;

public class CreateJellyFishCommandGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IJellyFishCommand.class).addBinding().to(CreateJellyFishCommandGuiceWrapper.class);

      Multibinder.newSetBinder(binder(), ICommand.class).addBinding().to(CreateJellyFishCommandGuiceWrapper.class);
   }
}
