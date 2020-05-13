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
package com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.jellyfish.service.sequence.api.ISequenceService;

/**
 * Guice module for the {@code ISequenceService} impl.
 */
public class SequenceServiceGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
   }

   /**
    * Configures the {@code ISequenceService} implementation.
    *
    * @param scenarioService the scenario service
    * @param logService      the log service
    * @return the sequence service implementation to use
    */
   @Provides
   public ISequenceService sequenceService(IScenarioService scenarioService,
                                           ILogService logService) {
      SequenceService service = new SequenceService();
      service.setScenarioService(scenarioService);
      service.setLogService(logService);
      service.activate();
      return service;
   }
}
