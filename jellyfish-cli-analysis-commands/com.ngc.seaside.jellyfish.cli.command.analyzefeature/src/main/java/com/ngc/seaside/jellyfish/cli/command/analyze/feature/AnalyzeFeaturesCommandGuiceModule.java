package com.ngc.seaside.jellyfish.cli.command.analyze.feature;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.service.analysis.api.ISystemDescriptorFindingType;

/**
 * The model for the analysis command and its finding types.
 */
public class AnalyzeFeaturesCommandGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IJellyFishCommand.class)
            .addBinding()
            .to(AnalyzeFeaturesCommandGuiceWrapper.class);

      // Register the finding types for this analysis.
      Multibinder<ISystemDescriptorFindingType> typesBinder = Multibinder.newSetBinder(
            binder(),
            ISystemDescriptorFindingType.class);
      for (ISystemDescriptorFindingType type : FeatureFindingTypes.values()) {
         typesBinder.addBinding().toInstance(type);
      }
   }
}
