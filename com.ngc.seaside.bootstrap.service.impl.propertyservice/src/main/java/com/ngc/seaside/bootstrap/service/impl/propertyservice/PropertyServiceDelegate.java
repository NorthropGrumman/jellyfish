package com.ngc.seaside.bootstrap.service.impl.propertyservice;

import com.ngc.seaside.bootstrap.service.property.api.IProperties;
import com.ngc.seaside.bootstrap.service.property.api.IPropertyService;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.IOException;
import java.nio.file.Path;

/**
 * This class is the default implementation of the IPropertyService interface.
 */
public class PropertyServiceDelegate implements IPropertyService {
   private final VelocityEngine engine = new VelocityEngine();
   private final VelocityContext context = new VelocityContext();

   /**
    * Constructor.
    */
   public PropertyServiceDelegate() {
      engine.setProperty("runtime.references.strict", true);
   }

   @Override
   public IProperties load(Path propertiesFile) throws IOException {
      Properties p = new Properties();
      p.load(propertiesFile);
      p.evaluate();
      return p;
   }

}
