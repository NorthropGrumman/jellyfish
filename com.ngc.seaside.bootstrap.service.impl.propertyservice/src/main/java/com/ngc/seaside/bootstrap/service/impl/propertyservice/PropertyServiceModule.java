package com.ngc.seaside.bootstrap.service.impl.propertyservice;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.property.api.IProperties;
import com.ngc.seaside.bootstrap.service.property.api.IPropertyService;

import java.io.IOException;
import java.nio.file.Path;

/**
 * The Guice module that wraps the PropertyService implementation.
 */
public class PropertyServiceModule extends AbstractModule implements IPropertyService {

   private final PropertyService delegate = new PropertyService();

   @Override
   protected void configure() {
      bind(IPropertyService.class).toInstance(this);
   }

   @Override
   public IProperties load(Path propertiesFile) throws IOException {
      return delegate.load(propertiesFile);
   }

   @Inject
   public void setLogService(ILogService ref) {
      delegate.setLogService(ref);
   }
}
