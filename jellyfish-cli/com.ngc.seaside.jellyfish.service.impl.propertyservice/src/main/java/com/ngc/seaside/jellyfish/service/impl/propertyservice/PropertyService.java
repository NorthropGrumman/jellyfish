/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.service.property.api.IProperties;
import com.ngc.seaside.jellyfish.service.property.api.IPropertyService;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Provide an OSGi enabled component representation of the IPropertyService interface.
 *
 * @see IPropertyService
 */
@Component(service = IPropertyService.class)
public class PropertyService implements IPropertyService {

   private ILogService logService;

   @Activate
   public void activate() {
      logService.trace(getClass(), "activated");
   }

   @Deactivate
   public void deactivate() {
      logService.trace(getClass(), "deactivated");
   }

   @Override
   public IProperties load(Path propertiesFile) throws IOException {
      logService.trace(getClass(), "loading properties %s", propertiesFile);
      Properties p = new Properties();
      p.load(propertiesFile);
      p.evaluate();
      return p;
   }

   /**
    * Sets log service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   /**
    * Remove log service.
    */
   public void removeLogService(ILogService ref) {
      setLogService(null);
   }
}
