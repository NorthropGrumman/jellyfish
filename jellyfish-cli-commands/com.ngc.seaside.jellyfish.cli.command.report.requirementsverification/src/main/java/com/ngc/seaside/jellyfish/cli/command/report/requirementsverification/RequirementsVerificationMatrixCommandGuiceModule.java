package com.ngc.seaside.jellyfish.cli.command.report.requirementsverification;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import com.ngc.seaside.jellyfish.api.IJellyFishCommand;

public class RequirementsVerificationMatrixCommandGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IJellyFishCommand.class)
            .addBinding()
            .to(RequirementsVerificationMatrixCommandGuiceWrapper.class);
   }
}
