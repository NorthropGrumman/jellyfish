package com.ngc.seaside.systemdescriptor.scenario.impl.module;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.SystemDescriptorRuntimeModule;
import com.ngc.seaside.systemdescriptor.scenario.api.IScenarioStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.AskStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.CompleteStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.PublishStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.ReceiveStepHandler;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
//import com.ngc.seaside.systemdescriptor.service.impl.xtext.XTextSystemDescriptorService;
//import com.ngc.seaside.systemdescriptor.service.impl.xtext.parsing.ParsingDelegate;
//import com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario.AskStepHandler;
//import com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario.CompleteStepHandler;
//import com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario.PublishStepHandler;
//import com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario.ReceiveStepHandler;
//import com.ngc.seaside.systemdescriptor.service.impl.xtext.validation.ScenarioStepValidator;
//import com.ngc.seaside.systemdescriptor.service.impl.xtext.validation.ValidationDelegate;
import com.ngc.seaside.systemdescriptor.validation.api.ISystemDescriptorValidator;

import org.eclipse.xtext.common.TerminalsStandaloneSetup;

import java.lang.reflect.Constructor;

/**
 * The Guice module configuration for the service.  Only use the {@link #StepsSystemDescriptorServiceModule() default
 * constructor} of this module if the service is being used within Eclipse.  Otherwise, use {@link
 * #forStandaloneUsage()} to create the module.
 */
public class StepsSystemDescriptorServiceModule extends AbstractModule {

//   private final Constructor<StepsSystemDescriptorServiceModule> constructor;
//   private final boolean isStandalone;
//
//   /**
//    * Creates a new instance of this module that is configured to provide a {@code ISystemDescriptorService} for use
//    * within Eclipse.  If the service is being used outside of Eclipse use {@link #forStandaloneUsage()} instead to
//    * create this module.
//    */
//   public StepsSystemDescriptorServiceModule() {
//      this(getEclipseIntegratedConstructor(), false);
//   }
//
//   /**
//    * Private constructor used to specify the constructor of the service which will be bound..
//    */
//   private StepsSystemDescriptorServiceModule(Constructor<XTextSystemDescriptorService> constructor,
//                                              boolean isStandalone) {
//      this.constructor = constructor;
//      this.isStandalone = isStandalone;
//   }


   /**
    * Invoked to bind the default {@link IScenarioStepHandler}s.
    */
   protected void bindDefaultStepHandlers() {
      // Step handlers should be singletons.
      bind(ReceiveStepHandler.class).in(Singleton.class);
      bind(AskStepHandler.class).in(Singleton.class);
      bind(PublishStepHandler.class).in(Singleton.class);
      bind(CompleteStepHandler.class).in(Singleton.class);

      Multibinder<IScenarioStepHandler> handlers = Multibinder.newSetBinder(
            binder(),
            IScenarioStepHandler.class);
      handlers.addBinding().to(ReceiveStepHandler.class);
      handlers.addBinding().to(AskStepHandler.class);
      handlers.addBinding().to(PublishStepHandler.class);
      handlers.addBinding().to(CompleteStepHandler.class);

      // These handlers are also validators.
      Multibinder<ISystemDescriptorValidator> validators = Multibinder.newSetBinder(
            binder(),
            ISystemDescriptorValidator.class);
      validators.addBinding().to(PublishStepHandler.class);
      validators.addBinding().to(ReceiveStepHandler.class);
      validators.addBinding().to(AskStepHandler.class);
      validators.addBinding().to(CompleteStepHandler.class);
   }

	@Override
	protected void configure() {
	    bindDefaultStepHandlers();
	}

}
