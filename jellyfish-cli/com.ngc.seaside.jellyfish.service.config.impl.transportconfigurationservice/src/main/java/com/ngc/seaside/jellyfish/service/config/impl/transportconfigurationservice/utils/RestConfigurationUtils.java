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
package com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice.utils;

import com.ngc.seaside.jellyfish.service.config.api.dto.HttpMethod;
import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;

import java.math.BigInteger;
import java.util.Objects;

public class RestConfigurationUtils extends CommonConfigurationUtils {

   public static final String REST_CONFIGURATION_QUALIFIED_NAME =
         "com.ngc.seaside.systemdescriptor.deployment.rest.RestConfiguration";
   public static final String SERVER_ADDRESS_FIELD_NAME = "serverAddress";
   public static final String SERVER_INTERFACE_FIELD_NAME = "serverInterface";
   public static final String PATH_FIELD_NAME = "path";
   public static final String CONTENT_TYPE_FIELD_NAME = "contentType";
   public static final String HTTP_METHOD_FIELD_NAME = "httpMethod";
   public static final String PORT_FIELD_NAME = "port";

   public static boolean isRestConfiguration(IData type) {
      return Objects.equals(REST_CONFIGURATION_QUALIFIED_NAME, type.getFullyQualifiedName());
   }

   /**
    * Converts the given property data value to a rest configuration.
    * 
    * @param value property value
    * @return the rest configuration
    */
   public static RestConfiguration getRestConfiguration(IPropertyDataValue value) {
      RestConfiguration configuration = new RestConfiguration();
      IPropertyDataValue serverAddressValue = value.getData(getField(value, SERVER_ADDRESS_FIELD_NAME));
      IPropertyDataValue serverInterfaceValue = value.getData(getField(value, SERVER_INTERFACE_FIELD_NAME));
      BigInteger port = value.getPrimitive(
            TransportConfigurationServiceUtils.getField(value, PORT_FIELD_NAME)).getInteger();
      String path = value.getPrimitive(TransportConfigurationServiceUtils.getField(value, PATH_FIELD_NAME)).getString();
      String contentType = value.getPrimitive(
            TransportConfigurationServiceUtils.getField(value, CONTENT_TYPE_FIELD_NAME))
                                .getString();
      String httpMethod = value.getEnumeration(
            TransportConfigurationServiceUtils.getField(value, HTTP_METHOD_FIELD_NAME)).getValue();
      configuration.setNetworkAddress(getNetworkAddress(serverAddressValue));
      configuration.setNetworkInterface(getNetworkInterface(serverInterfaceValue));
      configuration.setPort(port.intValueExact());
      configuration.setPath(path);
      configuration.setContentType(contentType);
      configuration.setHttpMethod(HttpMethod.valueOf(httpMethod));
      return configuration;
   }

}
