package com.ngc.seaside.bootstrap.service.impl.propertyservice;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.property.api.IProperties;
import com.ngc.seaside.bootstrap.service.property.api.IPropertyService;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Configure the service for use in Guice
 */
public class PropertyServiceGuiceModule extends AbstractModule {
   @Override
   protected void configure() {
      bind(IPropertyService.class).to(PropertyServiceGuiceWrapper.class);
   }

}
