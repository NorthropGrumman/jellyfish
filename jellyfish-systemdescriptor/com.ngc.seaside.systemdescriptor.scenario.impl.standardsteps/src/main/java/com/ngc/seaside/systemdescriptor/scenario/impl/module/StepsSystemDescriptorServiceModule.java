package com.ngc.seaside.systemdescriptor.scenario.impl.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;

import com.ngc.seaside.systemdescriptor.scenario.api.IScenarioStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.AskStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.BeginCorrleationEventStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.CompleteStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.CorrelateStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.PublishStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.ReceiveStepHandler;
import com.ngc.seaside.systemdescriptor.validation.api.ISystemDescriptorValidator;

/**
 * The Guice module configuration for the default step handlers.
 */
public class StepsSystemDescriptorServiceModule extends AbstractModule {

   /**
    * Invoked to bind the default {@link IScenarioStepHandler}s.
    */
   protected void bindDefaultStepHandlers() {
      // Step handlers should be singletons.
      bind(ReceiveStepHandler.class).in(Singleton.class);
      bind(AskStepHandler.class).in(Singleton.class);
      bind(PublishStepHandler.class).in(Singleton.class);
      bind(CompleteStepHandler.class).in(Singleton.class);
      bind(CorrelateStepHandler.class).in(Singleton.class);
      bind(BeginCorrleationEventStepHandler.class).in(Singleton.class);

      Multibinder<IScenarioStepHandler> handlers = Multibinder.newSetBinder(
            binder(),
            IScenarioStepHandler.class);
      handlers.addBinding().to(ReceiveStepHandler.class);
      handlers.addBinding().to(AskStepHandler.class);
      handlers.addBinding().to(PublishStepHandler.class);
      handlers.addBinding().to(CompleteStepHandler.class);
      handlers.addBinding().to(CorrelateStepHandler.class);
      handlers.addBinding().to(BeginCorrleationEventStepHandler.class);

      // These handlers are also validators.
      Multibinder<ISystemDescriptorValidator> validators = Multibinder.newSetBinder(
            binder(),
            ISystemDescriptorValidator.class);
      validators.addBinding().to(PublishStepHandler.class);
      validators.addBinding().to(ReceiveStepHandler.class);
      validators.addBinding().to(AskStepHandler.class);
      validators.addBinding().to(CompleteStepHandler.class);
      validators.addBinding().to(CorrelateStepHandler.class);
      validators.addBinding().to(BeginCorrleationEventStepHandler.class);
   }

   @Override
   protected void configure() {
      bindDefaultStepHandlers();
   }

}
