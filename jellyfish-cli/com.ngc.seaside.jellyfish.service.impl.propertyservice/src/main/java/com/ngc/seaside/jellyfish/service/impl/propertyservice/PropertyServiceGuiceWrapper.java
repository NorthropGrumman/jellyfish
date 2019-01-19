/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish.service.impl.propertyservice;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.service.property.api.IProperties;
import com.ngc.seaside.jellyfish.service.property.api.IPropertyService;

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
