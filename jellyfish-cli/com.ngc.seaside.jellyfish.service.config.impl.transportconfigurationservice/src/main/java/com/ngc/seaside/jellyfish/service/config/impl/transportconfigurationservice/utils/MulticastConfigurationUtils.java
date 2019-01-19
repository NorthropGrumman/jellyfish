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

import com.ngc.seaside.jellyfish.service.config.api.dto.MulticastConfiguration;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;

import java.math.BigInteger;
import java.util.Objects;

public class MulticastConfigurationUtils extends CommonConfigurationUtils {

   public static final String
         MULTICAST_CONFIGURATION_QUALIFIED_NAME =
         "com.ngc.seaside.systemdescriptor.deployment.multicast.MulticastConfiguration";
   public static final String GROUP_ADDRESS_FIELD_NAME = "groupAddress";
   public static final String PORT_FIELD_NAME = "port";
   public static final String SOURCE_INTERFACE_FIELD_NAME = "sourceInterface";
   public static final String TARGET_INTERFACE_FIELD_NAME = "targetInterface";

   public static boolean isMulticastConfiguration(IData type) {
      return Objects.equals(MULTICAST_CONFIGURATION_QUALIFIED_NAME, type.getFullyQualifiedName());
   }

   /**
    *
    * @param value The configuration used to create the multicast config
    * @return MulticastConfiguration based on the IPropertyDataValue
    */
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
