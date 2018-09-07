/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 * 
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
package com.test.model.tests.config;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.seaside.service.transport.api.ITransportService;
import com.test.model.transport.topic.ModelTransportTopics;

public class ModelTestTransportConfiguration{

   @SuppressWarnings("unused")
   private final ILogService logService;

   @SuppressWarnings("unused")
   private final IResourceService resourceService;

   @SuppressWarnings("unused")
   private final ITransportService transportService;
   @Inject
   public ModelTestTransportConfiguration(ILogService logService, IResourceService resourceService, ITransportService transportService ) {
      this.logService = logService;
      this.resourceService = resourceService;
      this.transportService = transportService;
      activate();
   }

   private void activate() {
      // TODO Implement configurations
   }
}
