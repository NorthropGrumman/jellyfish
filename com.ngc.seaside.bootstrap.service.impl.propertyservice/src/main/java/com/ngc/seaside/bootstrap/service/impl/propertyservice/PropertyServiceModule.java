package com.ngc.seaside.bootstrap.service.impl.propertyservice;

import com.google.inject.AbstractModule;

import com.ngc.seaside.bootstrap.service.property.api.IPropertyService;

/**
 * Created by jprovence on 6/6/2017.
 */
public class PropertyServiceModule extends AbstractModule {
   @Override
   protected void configure() {
      bind(IPropertyService.class).to(PropertyServiceDelegate.class);
   }
}
