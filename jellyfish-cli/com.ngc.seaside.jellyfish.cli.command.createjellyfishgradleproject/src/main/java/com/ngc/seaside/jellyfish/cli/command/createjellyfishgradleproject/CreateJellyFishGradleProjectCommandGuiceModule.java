package com.ngc.seaside.jellyfish.cli.command.createjellyfishgradleproject;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;

public class CreateJellyFishGradleProjectCommandGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IJellyFishCommand.class).addBinding()
               .to(CreateJellyFishGradleProjectCommandGuiceWrapper.class);

      Multibinder.newSetBinder(binder(), ICommand.class).addBinding()
               .to(CreateJellyFishGradleProjectCommandGuiceWrapper.class);
   }
}
