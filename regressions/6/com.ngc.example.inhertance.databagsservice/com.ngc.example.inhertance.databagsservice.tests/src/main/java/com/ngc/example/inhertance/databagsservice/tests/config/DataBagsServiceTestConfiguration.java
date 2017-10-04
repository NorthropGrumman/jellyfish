package com.ngc.example.inhertance.databagsservice.tests.config;

import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.seaside.service.transport.api.ITransportService;
import com.ngc.example.inhertance.databagsservice.transport.topic.DataBagsServiceTransportTopics;

public class DataBagsServiceTestConfiguration{

   @SuppressWarnings("unused")
   private final ILogService logService;

   @SuppressWarnings("unused")
   private final IResourceService resourceService;

   @SuppressWarnings("unused")
   private final ITransportService transportService;
   @Inject
   public DataBagsServiceTestConfiguration(ILogService logService, IResourceService resourceService, ITransportService transportService ) {
      this.logService = logService;
      this.resourceService = resourceService;
      this.transportService = transportService;
      activate();
   }

   private void activate() {
      // TODO Implement configurations
   }
}
