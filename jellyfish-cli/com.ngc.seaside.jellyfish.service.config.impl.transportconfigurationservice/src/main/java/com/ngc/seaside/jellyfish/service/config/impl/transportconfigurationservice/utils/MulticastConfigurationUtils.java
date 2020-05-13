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
