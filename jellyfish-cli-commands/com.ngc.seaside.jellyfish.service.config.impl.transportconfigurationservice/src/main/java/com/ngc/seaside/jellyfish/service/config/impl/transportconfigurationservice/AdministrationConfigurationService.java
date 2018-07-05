package com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice;

import com.ngc.seaside.jellyfish.service.config.api.IAdministrationConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.admin.AdministrationConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.admin.RestAdministrationConfiguration;
import com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice.utils.RestConfigurationUtils;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;

public class AdministrationConfigurationService
         extends ModelPropertyConfigurationService<AdministrationConfiguration>
         implements IAdministrationConfigurationService {

   public static final String REST_ADIMINSTRATION_CONFIGURATION_QUALIFIED_NAME =
            "com.ngc.seaside.systemdescriptor.administration.RestAdministration";
   
   public static final String SHUTDOWN_FIELD_NAME = "shutdown";
   public static final String RESTART_FIELD_NAME = "restart";
   
   @Override
   protected boolean isConfigurationProperty(String qualifiedName) {
      return REST_ADIMINSTRATION_CONFIGURATION_QUALIFIED_NAME.equals(qualifiedName);
   }

   @Override
   protected AdministrationConfiguration convert(IPropertyDataValue value) {
      String type = value.getReferencedDataType().getFullyQualifiedName();
      switch (type) {
         case REST_ADIMINSTRATION_CONFIGURATION_QUALIFIED_NAME:
            return convertRest(value);
         default:
            throw new IllegalArgumentException("Unable to convert " + type + " property to telemetry");
      }
   }

   private RestAdministrationConfiguration convertRest(IPropertyDataValue value) {
      RestAdministrationConfiguration configuration = new RestAdministrationConfiguration();
      IPropertyDataValue shutdownProperty = value.getData(getField(value, SHUTDOWN_FIELD_NAME));
      IPropertyDataValue restartProperty = value.getData(getField(value, RESTART_FIELD_NAME));
      RestConfiguration shutdownConfig = RestConfigurationUtils.getRestConfiguration(shutdownProperty);
      RestConfiguration restartConfig = RestConfigurationUtils.getRestConfiguration(restartProperty);
      configuration.setShutdown(shutdownConfig);
      configuration.setRestart(restartConfig);
      return configuration;
   }
   
}
