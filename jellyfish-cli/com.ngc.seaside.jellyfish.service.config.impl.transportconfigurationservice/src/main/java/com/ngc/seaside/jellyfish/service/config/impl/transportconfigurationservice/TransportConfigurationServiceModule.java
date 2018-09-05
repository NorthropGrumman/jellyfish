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
package com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.ngc.seaside.jellyfish.service.config.api.IAdministrationConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.ITelemetryConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.ITelemetryReportingConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;

public class TransportConfigurationServiceModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(ITransportConfigurationService.class).to(TransportConfigurationServiceGuiceWrapper.class)
               .in(Singleton.class);
   }

   /**
    * ITelemetryConfigurationService provider
    */
   @Provides
   @Singleton
   public ITelemetryConfigurationService getTelemetryConfigurationService(ISystemDescriptorService sdService) {
      TelemetryConfigurationService service = new TelemetryConfigurationService();
      service.setSystemDescriptorService(sdService);
      return service;
   }

   /**
    * ITelemetryReportingConfigurationService provider
    */
   @Provides
   @Singleton
   public ITelemetryReportingConfigurationService
            getTelemetryReportingConfigurationService(ISystemDescriptorService sdService) {
      TelemetryReportingConfigurationService service = new TelemetryReportingConfigurationService();
      service.setSystemDescriptorService(sdService);
      return service;
   }

   /**
    * IAdministrationConfigurationService provider
    */
   @Provides
   @Singleton
   public IAdministrationConfigurationService
            getAdministrationConfigurationService(ISystemDescriptorService sdService) {
      AdministrationConfigurationService service = new AdministrationConfigurationService();
      service.setSystemDescriptorService(sdService);
      return service;
   }

}
