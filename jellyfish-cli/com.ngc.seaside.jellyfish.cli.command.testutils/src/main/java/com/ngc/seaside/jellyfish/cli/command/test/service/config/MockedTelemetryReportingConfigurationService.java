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
package com.ngc.seaside.jellyfish.cli.command.test.service.config;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.config.api.ITelemetryConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.ITelemetryReportingConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.dto.HttpMethod;
import com.ngc.seaside.jellyfish.service.config.api.dto.NetworkAddress;
import com.ngc.seaside.jellyfish.service.config.api.dto.NetworkInterface;
import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.RestTelemetryReportingConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.TelemetryReportingConfiguration;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Collection;
import java.util.Optional;

/**
 * A basic implementation of {@link ITelemetryConfigurationService} for tests.
 */
public class MockedTelemetryReportingConfigurationService extends
         MockedModelPropertyConfigurationService<TelemetryReportingConfiguration>
         implements ITelemetryReportingConfigurationService {

   /**
    * Creates a rest telemetry reporting configuration.
    *
    * @param model model
    * @param rateInMilliseconds rate in milliseconds
    * @param address the address
    * @param interfaceName the interface name
    * @param port the port number
    * @param path the path name
    * @param contentType the content type
    * @param httpMethod the http method
    * @return a configured RestConfiguration
    */
   public RestTelemetryReportingConfiguration addRestTelemetryReportingConfiguration(IModel model,
            int rateInMilliseconds, String address, String interfaceName, int port, String path, String contentType,
            HttpMethod httpMethod) {
      RestConfiguration restConfiguration =
               new RestConfiguration().setNetworkAddress(new NetworkAddress().setAddress(address))
                        .setNetworkInterface(new NetworkInterface().setName(interfaceName))
                        .setPort(port)
                        .setPath(path)
                        .setContentType(contentType)
                        .setHttpMethod(httpMethod);
      RestTelemetryReportingConfiguration configuration = new RestTelemetryReportingConfiguration();
      configuration.setRateInMilliseconds(rateInMilliseconds);
      configuration.setConfig(restConfiguration);
      return addConfiguration(model, configuration);
   }

   @Override
   public Optional<String> getTransportTopicName(IJellyFishCommandOptions options, IModel model) {
      Collection<TelemetryReportingConfiguration> configs = getConfigurations(options, model);
      if (configs.isEmpty()) {
         return Optional.empty();
      }
      return Optional.of(model.getName().toUpperCase() + "_TELEMETRY_REPORTING");
   }

}
