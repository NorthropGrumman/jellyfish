package com.ngc.seaside.jellyfish.cli.command.help;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import com.ngc.seaside.jellyfish.api.ICommand;

public class HelpCommandGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), ICommand.class)
            .addBinding()
            .to(HelpCommandGuiceWrapper.class)
            .asEagerSingleton();
   }

}
