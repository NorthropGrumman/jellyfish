package com.ngc.seaside.bootstrap.service.impl.propertyservice;

import com.ngc.seaside.bootstrap.service.property.api.IProperties;
import com.ngc.seaside.bootstrap.service.property.api.IPropertyService;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

/**
 * Provide an OSGi enabled component representation of the IPropertyService interface.
 *
 * @see IPropertyService
 * @see PropertyServiceDelegate
 */
@Component(service = IPropertyService.class)
public class PropertyService implements IPropertyService {

   private final PropertyServiceDelegate delegate =
            new PropertyServiceDelegate();

   @Activate
   public void activate() {

   }

   @Deactivate
   public void deactivate() {

   }

   @Override
   public IProperties load(Path propertiesFile) throws IOException {
      return delegate.load(propertiesFile);
   }
}
