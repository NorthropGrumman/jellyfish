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
