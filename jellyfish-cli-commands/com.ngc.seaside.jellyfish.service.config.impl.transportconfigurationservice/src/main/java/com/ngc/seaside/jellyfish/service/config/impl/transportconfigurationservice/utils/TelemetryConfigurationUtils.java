package com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice.utils;

import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.RestTelemetryConfiguration;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;

import java.util.Arrays;

public class TelemetryConfigurationUtils {

   public static final String REST_TELEMETRY_CONFIGURATION_QUALIFIED_NAME = 
            "com.ngc.seaside.systemdescriptor.telemetry.RestTelemetry";

   public static final String CONFIG_FIELD_NAME = "config";

   public static boolean isTelemetryConfiguration(IData type) {
      return Arrays.asList(REST_TELEMETRY_CONFIGURATION_QUALIFIED_NAME).contains(type.getFullyQualifiedName());
   }

   /**
    *
    * @param value of the configuration type
    * @return ZeroMqConfiguration of the passed in value
    */
   public static RestTelemetryConfiguration getRestTelemetryConfiguration(IPropertyDataValue value) {
      RestTelemetryConfiguration configuration = new RestTelemetryConfiguration();
      IPropertyDataValue restProperty = value.getData(getField(value, CONFIG_FIELD_NAME));
      RestConfiguration restConfig = RestConfigurationUtils.getRestConfiguration(restProperty);
      configuration.setConfig(restConfig);
      return configuration;
   }

   static IDataField getField(IPropertyDataValue value, String fieldName) {
      return value.getFieldByName(fieldName)
            .orElseThrow(() -> new IllegalStateException("Missing " + fieldName + " field"));
   }

}
