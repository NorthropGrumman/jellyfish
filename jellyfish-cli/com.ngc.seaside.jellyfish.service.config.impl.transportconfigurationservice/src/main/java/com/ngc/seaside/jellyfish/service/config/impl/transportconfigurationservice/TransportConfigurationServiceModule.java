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
