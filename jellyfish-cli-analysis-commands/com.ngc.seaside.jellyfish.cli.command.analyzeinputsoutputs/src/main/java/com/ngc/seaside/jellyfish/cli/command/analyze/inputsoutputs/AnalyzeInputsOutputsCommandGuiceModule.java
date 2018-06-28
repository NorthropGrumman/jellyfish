package com.ngc.seaside.jellyfish.cli.command.analyze.inputsoutputs;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.service.analysis.api.ISystemDescriptorFindingType;

public class AnalyzeInputsOutputsCommandGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IJellyFishCommand.class)
            .addBinding()
            .to(AnalyzeInputsOutputsCommandGuiceWrapper.class);

      // Register the finding types for this analysis.
      Multibinder<ISystemDescriptorFindingType> typesBinder = Multibinder.newSetBinder(
            binder(),
            ISystemDescriptorFindingType.class);
      for (ISystemDescriptorFindingType type : InputsOutputsFindingTypes.values()) {
         typesBinder.addBinding().toInstance(type);
      }
   }
}
