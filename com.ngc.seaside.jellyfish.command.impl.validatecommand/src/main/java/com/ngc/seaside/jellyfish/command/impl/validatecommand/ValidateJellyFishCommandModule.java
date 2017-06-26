package com.ngc.seaside.jellyfish.command.impl.validatecommand;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.ngc.seaside.command.api.ICommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;

/**
 * @author blake.perkins@ngc.com
 */
public class ValidateJellyFishCommandModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IJellyFishCommand.class)
               .addBinding()
               .to(ValidateJellyFishCommand.class);

      Multibinder.newSetBinder(binder(), ICommand.class)
               .addBinding()
               .to(ValidateJellyFishCommand.class);
   }
}
