package com.ngc.seaside.jellyfish.service.data.module;

import com.google.inject.AbstractModule;
import com.ngc.seaside.jellyfish.service.data.api.IDataService;
import com.ngc.seaside.jellyfish.service.data.impl.dataservice.DataServiceGuiceWrapper;

public class DataServicesGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(IDataService.class).to(DataServiceGuiceWrapper.class);
   }
}
