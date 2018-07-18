package com.ngc.seaside.jellyfish.cli.command.analyzebudget;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.cli.command.analyzebudget.budget.BudgetValidator;
import com.ngc.seaside.jellyfish.cli.command.analyzebudget.budget.SdBudgetAdapter;
import com.ngc.seaside.jellyfish.service.analysis.api.ISystemDescriptorFindingType;
import com.ngc.seaside.systemdescriptor.validation.api.ISystemDescriptorValidator;

import javax.inject.Singleton;

public class AnalyzeBudgetCommandGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IJellyFishCommand.class)
               .addBinding()
               .to(AnalyzeBudgetCommandGuiceWrapper.class);

      // Register the finding types for this analysis.
      Multibinder<ISystemDescriptorFindingType> typesBinder = Multibinder.newSetBinder(
               binder(),
               ISystemDescriptorFindingType.class);
      for (ISystemDescriptorFindingType type : BudgetFindingTypes.values()) {
         typesBinder.addBinding().toInstance(type);
      }

      bind(SdBudgetAdapter.class).in(Singleton.class);
      bind(BudgetValidator.class).in(Singleton.class);
      
      Multibinder<ISystemDescriptorValidator> validators = Multibinder.newSetBinder(
               binder(),
               ISystemDescriptorValidator.class);
      validators.addBinding().to(BudgetValidator.class);
   }

}
