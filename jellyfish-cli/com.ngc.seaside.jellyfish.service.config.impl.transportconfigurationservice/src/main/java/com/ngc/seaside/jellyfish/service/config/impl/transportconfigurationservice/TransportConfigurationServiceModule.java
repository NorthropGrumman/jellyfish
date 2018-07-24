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
