package com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice;

import com.ngc.seaside.jellyfish.service.config.api.dto.HttpMethod;
import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;

import java.math.BigInteger;
import java.util.Objects;

public class RestConfigurationUtils extends CommonConfigurationUtils {

   static final String REST_CONFIGURATION_QUALIFIED_NAME = "com.ngc.seaside.systemdescriptor.deployment.rest.RestConfiguration";
   static final String SERVER_ADDRESS_FIELD_NAME = "serverAddress";
   static final String SERVER_INTERFACE_FIELD_NAME = "serverInterface";
   static final String PATH_FIELD_NAME = "path";
   static final String CONTENT_TYPE_FIELD_NAME = "contentType";
   static final String HTTP_METHOD_FIELD_NAME = "httpMethod";
   static final String PORT_FIELD_NAME = "port";

   static boolean isRestConfiguration(IData type) {
      return Objects.equals(REST_CONFIGURATION_QUALIFIED_NAME, type.getFullyQualifiedName());
   }

   static RestConfiguration getRestConfiguration(IPropertyDataValue value) {
      RestConfiguration configuration = new RestConfiguration();
      IPropertyDataValue serverAddressValue = value.getData(getField(value, SERVER_ADDRESS_FIELD_NAME));
      IPropertyDataValue serverInterfaceValue = value.getData(getField(value, SERVER_INTERFACE_FIELD_NAME));
      BigInteger port = value.getPrimitive(TransportConfigurationService.getField(value, PORT_FIELD_NAME)).getInteger();
      String path = value.getPrimitive(TransportConfigurationService.getField(value, PATH_FIELD_NAME)).getString();
      String contentType = value.getPrimitive(TransportConfigurationService.getField(value, CONTENT_TYPE_FIELD_NAME))
                                .getString();
      String httpMethod = value.getEnumeration(TransportConfigurationService.getField(value, HTTP_METHOD_FIELD_NAME))
                               .getValue();
      configuration.setNetworkAddress(getNetworkAddress(serverAddressValue));
      configuration.setNetworkInterface(getNetworkInterface(serverInterfaceValue));
      configuration.setPort(port.intValueExact());
      configuration.setPath(path);
      configuration.setContentType(contentType);
      configuration.setHttpMethod(HttpMethod.valueOf(httpMethod));
      return configuration;
   }

}
