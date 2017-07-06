package com.ngc.seaside.jellyfish.cli.command.help;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.ngc.seaside.command.api.ICommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;

public class HelpCommandGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IJellyFishCommand.class).addBinding().to(HelpCommandGuiceWrapper.class).asEagerSingleton();;
      Multibinder.newSetBinder(binder(), ICommand.class).addBinding().to(HelpCommandGuiceWrapper.class).asEagerSingleton();;
   }

}
