package com.ngc.seaside.bootstrap.service.impl.propertyservice;

import com.google.inject.AbstractModule;
import com.ngc.seaside.bootstrap.service.property.api.IPropertyService;

/**
 * Configure the service for use in Guice
 */
public class PropertyServiceGuiceModule extends AbstractModule {
   @Override
   protected void configure() {
      bind(IPropertyService.class).to(PropertyServiceGuiceWrapper.class);
   }

}
