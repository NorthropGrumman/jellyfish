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
package com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.config.api.ITelemetryReportingConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.RestTelemetryReportingConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.TelemetryReportingConfiguration;
import com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice.utils.RestConfigurationUtils;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;

import java.util.Collection;
import java.util.Optional;
import java.util.regex.Pattern;

public class TelemetryReportingConfigurationService
         extends ModelPropertyConfigurationService<TelemetryReportingConfiguration>
         implements ITelemetryReportingConfigurationService {

   private static final Pattern[] PATTERNS = { Pattern.compile("([a-z\\d])([A-Z]+)"),
      Pattern.compile("([A-Z])([A-Z][a-z\\d])") };
   private static final String[] REPLACEMENTS = { "$1_$2", "$1_$2" };
   
   public static final String REST_TELEMETRY_REPORTING_CONFIGURATION_QUALIFIED_NAME =
            "com.ngc.seaside.systemdescriptor.telemetry.RestTelemetryReporting";

   public static final String RATE_FIELD_NAME = "rateInMilliseconds";
   public static final String CONFIG_FIELD_NAME = "config";

   @Override
   public Optional<String> getTransportTopicName(IJellyFishCommandOptions options, IModel model) {
      Collection<TelemetryReportingConfiguration> configs = getConfigurations(options, model);
      if (configs.isEmpty()) {
         return Optional.empty();
      }
      String topic = model.getName();
      for (int i = 0; i < PATTERNS.length; i++) {
         topic = PATTERNS[i].matcher(topic).replaceAll(REPLACEMENTS[i]);
      }
      return Optional.of(topic.toUpperCase() + "_TELEMETRY_REPORTING");
   }

   @Override
   protected boolean isConfigurationProperty(String qualifiedName) {
      return REST_TELEMETRY_REPORTING_CONFIGURATION_QUALIFIED_NAME.equals(qualifiedName);
   }

   @Override
   protected TelemetryReportingConfiguration convert(IPropertyDataValue value) {
      String type = value.getReferencedDataType().getFullyQualifiedName();

      switch (type) {
         case REST_TELEMETRY_REPORTING_CONFIGURATION_QUALIFIED_NAME:
            return convertRest(value);
         default:
            throw new IllegalArgumentException("Unable to convert " + type + " property to telemetry");
      }
   }

   private TelemetryReportingConfiguration convertRest(IPropertyDataValue value) {
      RestTelemetryReportingConfiguration configuration = new RestTelemetryReportingConfiguration();
      int rate = value.getPrimitive(getField(value, RATE_FIELD_NAME)).getInteger().intValueExact();
      IPropertyDataValue restProperty = value.getData(getField(value, CONFIG_FIELD_NAME));
      RestConfiguration restConfig = RestConfigurationUtils.getRestConfiguration(restProperty);
      configuration.setConfig(restConfig);
      configuration.setRateInMilliseconds(rate);
      return configuration;
   }
   
}
