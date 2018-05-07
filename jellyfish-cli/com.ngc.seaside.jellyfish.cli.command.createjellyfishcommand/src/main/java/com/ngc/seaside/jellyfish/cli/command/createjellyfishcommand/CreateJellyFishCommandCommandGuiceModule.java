package com.ngc.seaside.jellyfish.cli.command.createjellyfishcommand;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;

public class CreateJellyFishCommandCommandGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), ICommand.class)
            .addBinding()
            .to(CreateJellyFishCommandCommandGuiceWrapper.class);
   }
}
