package com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice.utils;

import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.RestTelemetryReportingConfiguration;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;

import java.util.Arrays;

public class TelemetryReportingConfigurationUtils {

   public static final String REST_TELEMETRY_REPORTING_CONFIGURATION_QUALIFIED_NAME =
            "com.ngc.seaside.systemdescriptor.telemetry.RestTelemetryReporting";

   public static final String RATE_FIELD_NAME = "rateInSeconds";
   public static final String CONFIG_FIELD_NAME = "config";

   public static boolean isTelemetryConfiguration(IData type) {
      return Arrays.asList(REST_TELEMETRY_REPORTING_CONFIGURATION_QUALIFIED_NAME)
               .contains(type.getFullyQualifiedName());
   }

   /**
    *
    * @param value of the configuration type
    * @return RestTelemetryReportingConfiguration of the passed in value
    */
   public static RestTelemetryReportingConfiguration getRestTelemetryReportingConfiguration(IPropertyDataValue value) {
      RestTelemetryReportingConfiguration configuration = new RestTelemetryReportingConfiguration();
      int rate = value.getPrimitive(getField(value, RATE_FIELD_NAME)).getInteger().intValueExact();
      IPropertyDataValue restProperty = value.getData(getField(value, CONFIG_FIELD_NAME));
      RestConfiguration restConfig = RestConfigurationUtils.getRestConfiguration(restProperty);
      configuration.setConfig(restConfig);
      configuration.setRateInSeconds(rate);
      return configuration;
   }

   static IDataField getField(IPropertyDataValue value, String fieldName) {
      return value.getFieldByName(fieldName)
               .orElseThrow(() -> new IllegalStateException("Missing " + fieldName + " field"));
   }

}
