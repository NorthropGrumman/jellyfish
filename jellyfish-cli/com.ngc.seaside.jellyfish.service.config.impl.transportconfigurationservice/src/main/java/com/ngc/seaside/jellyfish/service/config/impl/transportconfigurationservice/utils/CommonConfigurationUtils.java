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
package com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice.utils;

import com.ngc.seaside.jellyfish.service.config.api.dto.NetworkAddress;
import com.ngc.seaside.jellyfish.service.config.api.dto.NetworkInterface;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;

import java.util.Objects;

public class CommonConfigurationUtils {

   public static final String
         NETWORK_INTERFACE_QUALIFIED_NAME =
         "com.ngc.seaside.systemdescriptor.deployment.NetworkInterface";
   public static final String NETWORK_ADDRESS_QUALIFIED_NAME =
         "com.ngc.seaside.systemdescriptor.deployment.NetworkAddress";
   public static final String NETWORK_INTERFACE_NAME_FIELD_NAME = "name";
   public static final String NETWORK_ADDRESS_ADDRESS_FIELD_NAME = "address";

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
