package com.ngc.seaside.jellyfish.cli.command.createjavaserviceconnectorcommand;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import com.ngc.seaside.command.api.ICommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;

public class CreateJavaServiceConnectorCommandGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IJellyFishCommand.class).addBinding()
            .to(CreateJavaServiceConnectorCommandGuiceWrapper.class);
      Multibinder.newSetBinder(binder(), ICommand.class).addBinding()
            .to(CreateJavaServiceConnectorCommandGuiceWrapper.class);
   }
}
