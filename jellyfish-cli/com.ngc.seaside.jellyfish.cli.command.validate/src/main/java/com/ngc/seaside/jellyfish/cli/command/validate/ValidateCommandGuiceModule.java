package com.ngc.seaside.jellyfish.cli.command.validate;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;

/**
 * @author blake.perkins@ngc.com
 */
public class ValidateCommandGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IJellyFishCommand.class).addBinding().to(ValidateCommandGuiceWrapper.class);

      Multibinder.newSetBinder(binder(), ICommand.class).addBinding().to(ValidateCommandGuiceWrapper.class);
   }
}
