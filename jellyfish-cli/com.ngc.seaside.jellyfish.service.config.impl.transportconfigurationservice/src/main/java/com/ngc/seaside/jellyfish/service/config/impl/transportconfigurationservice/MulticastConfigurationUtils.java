package com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice;

import com.ngc.seaside.jellyfish.service.config.api.dto.MulticastConfiguration;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;

import java.math.BigInteger;
import java.util.Objects;

public class MulticastConfigurationUtils extends CommonConfigurationUtils {

   static final String MULTICAST_CONFIGURATION_QUALIFIED_NAME = "com.ngc.seaside.deployment.multicast.MulticastConfiguration";
   static final String GROUP_ADDRESS_FIELD_NAME = "groupAddress";
   static final String PORT_FIELD_NAME = "port";
   static final String SOURCE_INTERFACE_FIELD_NAME = "sourceInterface";
   static final String TARGET_INTERFACE_FIELD_NAME = "targetInterface";

   public static boolean isMulticastConfiguration(IData type) {
      return Objects.equals(MULTICAST_CONFIGURATION_QUALIFIED_NAME, type.getFullyQualifiedName());
   }

   public static MulticastConfiguration getMulticastConfiguration(IPropertyDataValue value) {
      MulticastConfiguration configuration = new MulticastConfiguration();
      IPropertyDataValue sourceInterfaceValue = value.getData(getField(value, SOURCE_INTERFACE_FIELD_NAME));
      IPropertyDataValue targetInterfaceValue = value.getData(getField(value, TARGET_INTERFACE_FIELD_NAME));
      String groupAddress = value.getPrimitive(getField(value, GROUP_ADDRESS_FIELD_NAME)).getString();
      BigInteger port = value.getPrimitive(getField(value, PORT_FIELD_NAME)).getInteger();
      configuration.setSourceInterface(getNetworkInterface(sourceInterfaceValue));
      configuration.setTargetInterface(getNetworkInterface(targetInterfaceValue));
      configuration.setGroupAddress(groupAddress);
      configuration.setPort(port.intValueExact());
      return configuration;
   }

}
