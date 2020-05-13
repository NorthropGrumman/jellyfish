/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
