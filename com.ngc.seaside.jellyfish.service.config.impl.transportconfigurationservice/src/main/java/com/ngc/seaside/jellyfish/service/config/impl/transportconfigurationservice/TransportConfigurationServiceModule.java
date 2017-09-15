package com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;

public class TransportConfigurationServiceModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(ITransportConfigurationService.class).to(TransportConfigurationServiceGuiceWrapper.class)
                                                .in(Singleton.class);
   }

}
