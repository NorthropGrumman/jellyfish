package com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice;

import com.ngc.seaside.jellyfish.service.config.api.dto.NetworkAddress;
import com.ngc.seaside.jellyfish.service.config.api.dto.NetworkInterface;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;

import java.util.Objects;

public class CommonConfigurationUtils {

   static final String
         NETWORK_INTERFACE_QUALIFIED_NAME =
         "com.ngc.seaside.systemdescriptor.deployment.NetworkInterface";
   static final String NETWORK_ADDRESS_QUALIFIED_NAME = "com.ngc.seaside.systemdescriptor.deployment.NetworkAddress";
   static final String NETWORK_INTERFACE_NAME_FIELD_NAME = "name";
   static final String NETWORK_ADDRESS_ADDRESS_FIELD_NAME = "address";

   static NetworkInterface getNetworkInterface(IPropertyDataValue value) {
      if (!Objects.equals(NETWORK_INTERFACE_QUALIFIED_NAME, value.getReferencedDataType().getFullyQualifiedName())) {
         throw new IllegalArgumentException(
               "Property " + value + " is not of type " + NETWORK_INTERFACE_QUALIFIED_NAME);
      }
      String name = value.getPrimitive(getField(value, NETWORK_INTERFACE_NAME_FIELD_NAME)).getString();
      return new NetworkInterface(name);
   }

   static NetworkAddress getNetworkAddress(IPropertyDataValue value) {
      if (!Objects.equals(NETWORK_ADDRESS_QUALIFIED_NAME, value.getReferencedDataType().getFullyQualifiedName())) {
         throw new IllegalArgumentException("Property " + value + " is not of type " + NETWORK_ADDRESS_QUALIFIED_NAME);
      }
      String address = value.getPrimitive(getField(value, NETWORK_ADDRESS_ADDRESS_FIELD_NAME)).getString();
      return new NetworkAddress().setAddress(address);
   }

   static IDataField getField(IPropertyDataValue value, String fieldName) {
      return value.getFieldByName(fieldName)
            .orElseThrow(() -> new IllegalStateException("Missing " + fieldName + " field"));
   }

}
