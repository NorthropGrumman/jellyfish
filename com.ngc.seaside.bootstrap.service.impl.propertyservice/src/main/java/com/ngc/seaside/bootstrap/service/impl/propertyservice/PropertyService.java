package com.ngc.seaside.bootstrap.service.impl.propertyservice;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.property.api.IProperties;
import com.ngc.seaside.bootstrap.service.property.api.IPropertyService;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

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
