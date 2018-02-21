package com.ngc.example.correlation.cs.tests.config;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.seaside.service.transport.api.ITransportService;
import com.ngc.example.correlation.cs.transport.topic.CandyServiceTransportTopics;

public class CandyServiceTestConfiguration{

   @SuppressWarnings("unused")
   private final ILogService logService;

   @SuppressWarnings("unused")
   private final IResourceService resourceService;

   @SuppressWarnings("unused")
   private final ITransportService transportService;
   @Inject
   public CandyServiceTestConfiguration(ILogService logService, IResourceService resourceService, ITransportService transportService ) {
      this.logService = logService;
      this.resourceService = resourceService;
      this.transportService = transportService;
      activate();
   }

   private void activate() {
      // TODO Implement configurations
   }
}
