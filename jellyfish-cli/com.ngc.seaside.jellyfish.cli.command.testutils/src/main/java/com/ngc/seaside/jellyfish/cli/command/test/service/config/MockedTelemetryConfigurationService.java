/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.jellyfish.cli.command.test.service.config;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.config.api.ITelemetryConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.dto.HttpMethod;
import com.ngc.seaside.jellyfish.service.config.api.dto.NetworkAddress;
import com.ngc.seaside.jellyfish.service.config.api.dto.NetworkInterface;
import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.RestTelemetryConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.TelemetryConfiguration;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;

import java.util.Collection;
import java.util.Optional;

/**
 * A basic implementation of {@link ITelemetryConfigurationService} for tests.
 */
public class MockedTelemetryConfigurationService extends
         MockedModelPropertyConfigurationService<TelemetryConfiguration> implements ITelemetryConfigurationService {

   /**
    * Creates a rest telemetry configuration.
    *
    * @param model model
    * @param address the address
    * @param interfaceName the interface name
    * @param port the port number
    * @param path the path name
    * @param contentType the content type
    * @param httpMethod the http method
    * @return a configured RestConfiguration
    */
   public RestTelemetryConfiguration addRestTelemetryConfiguration(IModel model, String address,
            String interfaceName, int port, String path,
            String contentType, HttpMethod httpMethod) {
      RestConfiguration restConfiguration =
               new RestConfiguration().setNetworkAddress(new NetworkAddress().setAddress(address))
                        .setNetworkInterface(new NetworkInterface().setName(interfaceName))
                        .setPort(port)
                        .setPath(path)
                        .setContentType(contentType)
                        .setHttpMethod(httpMethod);
      RestTelemetryConfiguration configuration = new RestTelemetryConfiguration();
      configuration.setConfig(restConfiguration);
      return addConfiguration(model, configuration);
   }

   @Override
   public Optional<String> getTransportTopicName(IJellyFishCommandOptions options, IModelReferenceField part) {
      Collection<TelemetryConfiguration> configs = getConfigurations(options, part.getType());
      if (configs.isEmpty()) {
         return Optional.empty();
      }
      return Optional.of(part.getName().toUpperCase() + "_TELEMETRY");
   }

}
