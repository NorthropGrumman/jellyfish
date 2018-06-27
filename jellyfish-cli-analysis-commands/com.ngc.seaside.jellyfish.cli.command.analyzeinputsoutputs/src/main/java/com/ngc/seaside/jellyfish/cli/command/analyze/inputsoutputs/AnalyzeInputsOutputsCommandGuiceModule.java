package com.ngc.seaside.jellyfish.cli.command.analyze.inputsoutputs;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import com.ngc.seaside.jellyfish.api.IJellyFishCommand;

public class AnalyzeInputsOutputsCommandGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IJellyFishCommand.class)
            .addBinding()
            .to(AnalyzeInputsOutputsCommandGuiceWrapper.class);
   }
}
