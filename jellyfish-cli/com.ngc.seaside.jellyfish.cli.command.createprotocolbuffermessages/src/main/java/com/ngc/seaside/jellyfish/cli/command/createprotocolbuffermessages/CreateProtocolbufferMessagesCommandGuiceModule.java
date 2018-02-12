package com.ngc.seaside.jellyfish.cli.command.createprotocolbuffermessages;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;

/**
 * Configure the service for use in Guice
 */
public class CreateProtocolbufferMessagesCommandGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IJellyFishCommand.class).addBinding()
               .to(CreateProtocolbufferMessagesCommandGuiceWrapper.class);
      Multibinder.newSetBinder(binder(), ICommand.class).addBinding()
               .to(CreateProtocolbufferMessagesCommandGuiceWrapper.class);
   }
}
