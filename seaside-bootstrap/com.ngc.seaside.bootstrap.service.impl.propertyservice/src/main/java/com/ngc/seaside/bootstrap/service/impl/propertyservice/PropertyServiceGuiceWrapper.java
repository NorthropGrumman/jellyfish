package com.ngc.seaside.bootstrap.service.impl.propertyservice;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.property.api.IProperties;
import com.ngc.seaside.bootstrap.service.property.api.IPropertyService;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Wrap the service using Guice Injection
 */
@Singleton
public class PropertyServiceGuiceWrapper implements IPropertyService {

   private final PropertyService delegate = new PropertyService();

   @Inject
   public PropertyServiceGuiceWrapper(ILogService logService) {
      delegate.setLogService(logService);
      delegate.activate();
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
