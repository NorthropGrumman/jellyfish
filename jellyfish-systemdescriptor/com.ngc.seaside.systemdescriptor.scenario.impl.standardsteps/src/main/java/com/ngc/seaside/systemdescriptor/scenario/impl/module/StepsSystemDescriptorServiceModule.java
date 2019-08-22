/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.systemdescriptor.scenario.impl.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;

import com.ngc.seaside.systemdescriptor.scenario.api.IScenarioStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.BeginCorrelationEventStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.CompleteStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.CorrelateStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.PublishStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.ReceiveRequestStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.ReceiveStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.RespondStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.TodoStepHandler;
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
      bind(PublishStepHandler.class).in(Singleton.class);
      bind(CompleteStepHandler.class).in(Singleton.class);
      bind(CorrelateStepHandler.class).in(Singleton.class);
      bind(BeginCorrelationEventStepHandler.class).in(Singleton.class);
      bind(ReceiveRequestStepHandler.class).in(Singleton.class);
      bind(RespondStepHandler.class).in(Singleton.class);
      bind(TodoStepHandler.class).in(Singleton.class);

      Multibinder<IScenarioStepHandler> handlers = Multibinder.newSetBinder(
            binder(),
            IScenarioStepHandler.class);
      handlers.addBinding().to(ReceiveStepHandler.class);
      handlers.addBinding().to(PublishStepHandler.class);
      handlers.addBinding().to(CompleteStepHandler.class);
      handlers.addBinding().to(CorrelateStepHandler.class);
      handlers.addBinding().to(BeginCorrelationEventStepHandler.class);
      handlers.addBinding().to(ReceiveRequestStepHandler.class);
      handlers.addBinding().to(RespondStepHandler.class);
      handlers.addBinding().to(TodoStepHandler.class);

      // These handlers are also validators.
      Multibinder<ISystemDescriptorValidator> validators = Multibinder.newSetBinder(
            binder(),
            ISystemDescriptorValidator.class);
      validators.addBinding().to(PublishStepHandler.class);
      validators.addBinding().to(ReceiveStepHandler.class);
      validators.addBinding().to(CompleteStepHandler.class);
      validators.addBinding().to(CorrelateStepHandler.class);
      validators.addBinding().to(BeginCorrelationEventStepHandler.class);
      validators.addBinding().to(ReceiveRequestStepHandler.class);
      validators.addBinding().to(RespondStepHandler.class);
      validators.addBinding().to(TodoStepHandler.class);
   }

   @Override
   protected void configure() {
      bindDefaultStepHandlers();
   }

}
