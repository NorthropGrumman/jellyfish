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
