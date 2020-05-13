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
package com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice;

import com.ngc.seaside.jellyfish.service.config.api.dto.HttpMethod;
import com.ngc.seaside.jellyfish.service.config.api.dto.zeromq.ConnectionType;
import com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice.utils.CommonConfigurationUtils;
import com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice.utils.MulticastConfigurationUtils;
import com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice.utils.RestConfigurationUtils;
import com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice.utils.ZeroMqConfigurationUtils;
import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConfigurationTestUtils {

   static IModelLink<IDataReferenceField> getMockedLink(IDataReferenceField field, boolean fieldIsSource,
            IProperty... properties) {
      IModelLink<IDataReferenceField> link =
               mock(TransportConfigurationServiceTest.DataReferenceFieldLink.class, RETURNS_DEEP_STUBS);

      IDataReferenceField source;
      IDataReferenceField target;
      if (fieldIsSource) {
         source = field;
         target = mock(IDataReferenceField.class);
      } else {
         source = mock(IDataReferenceField.class);
         target = field;
      }
      when(link.getSource()).thenReturn(source);
      when(link.getTarget()).thenReturn(target);
      when(link.getRefinedLink()).thenReturn(Optional.empty());
      when(link.getMetadata()).thenReturn(IMetadata.EMPTY_METADATA);
      when(link.getProperties().iterator()).thenReturn(Arrays.asList(properties).iterator());
      when(link.getProperties().stream()).thenAnswer(args -> Stream.of(properties));
      return link;
   }

   static IProperty getMockedMulticastConfiguration(String groupAddress, int port, String sourceInterfaceName,
            String targetInterfaceName) {
      IProperty property = mock(IProperty.class, RETURNS_DEEP_STUBS);
      when(property.getName()).thenReturn(UUID.randomUUID().toString());
      when(property.getCardinality()).thenReturn(FieldCardinality.SINGLE);
      when(property.getType()).thenReturn(DataTypes.DATA);
      when(property.getReferencedDataType().getFullyQualifiedName()).thenReturn(
               MulticastConfigurationUtils.MULTICAST_CONFIGURATION_QUALIFIED_NAME);
      IDataField groupAddressField = mock(IDataField.class);
      IDataField portField = mock(IDataField.class);
      IDataField sourceField = mock(IDataField.class);
      IDataField targetField = mock(IDataField.class);
      when(property.getData().isSet()).thenReturn(true);
      when(property.getData().getFieldByName(MulticastConfigurationUtils.GROUP_ADDRESS_FIELD_NAME)).thenReturn(
               Optional.of(groupAddressField));
      when(property.getData().getFieldByName(MulticastConfigurationUtils.PORT_FIELD_NAME)).thenReturn(
               Optional.of(portField));
      when(property.getData().getFieldByName(MulticastConfigurationUtils.SOURCE_INTERFACE_FIELD_NAME)).thenReturn(
               Optional.of(sourceField));
      when(property.getData().getFieldByName(MulticastConfigurationUtils.TARGET_INTERFACE_FIELD_NAME)).thenReturn(
               Optional.of(targetField));
      when(property.getData().getPrimitive(groupAddressField).getString()).thenReturn(groupAddress);
      when(property.getData().getPrimitive(portField).getInteger()).thenReturn(BigInteger.valueOf(port));
      IPropertyDataValue sourceInterface = ConfigurationTestUtils.getMockedNetworkInterface(sourceInterfaceName);
      IPropertyDataValue targetInterface = ConfigurationTestUtils.getMockedNetworkInterface(targetInterfaceName);
      when(property.getData().getData(sourceField)).thenReturn(sourceInterface);
      when(property.getData().getData(targetField)).thenReturn(targetInterface);
      return property;
   }

   static IProperty getMockedRestConfiguration(String address, String interfaceName, int port, String path,
            String contentType, HttpMethod method) {
      IProperty property = mock(IProperty.class, RETURNS_DEEP_STUBS);
      when(property.getName()).thenReturn(UUID.randomUUID().toString());
      when(property.getCardinality()).thenReturn(FieldCardinality.SINGLE);
      when(property.getType()).thenReturn(DataTypes.DATA);
      when(property.getReferencedDataType().getFullyQualifiedName()).thenReturn(
               RestConfigurationUtils.REST_CONFIGURATION_QUALIFIED_NAME);
      IDataField addressField = mock(IDataField.class);
      IDataField interfaceField = mock(IDataField.class);
      IDataField portField = mock(IDataField.class);
      IDataField pathField = mock(IDataField.class);
      IDataField contentTypeField = mock(IDataField.class);
      IDataField httpMethodField = mock(IDataField.class);
      when(property.getData().isSet()).thenReturn(true);
      when(property.getData().getFieldByName(RestConfigurationUtils.SERVER_ADDRESS_FIELD_NAME)).thenReturn(
               Optional.of(addressField));
      when(property.getData().getFieldByName(RestConfigurationUtils.SERVER_INTERFACE_FIELD_NAME)).thenReturn(
               Optional.of(interfaceField));
      when(property.getData().getFieldByName(RestConfigurationUtils.PORT_FIELD_NAME)).thenReturn(
               Optional.of(portField));
      when(property.getData().getFieldByName(RestConfigurationUtils.PATH_FIELD_NAME)).thenReturn(
               Optional.of(pathField));
      when(property.getData().getFieldByName(RestConfigurationUtils.CONTENT_TYPE_FIELD_NAME)).thenReturn(
               Optional.of(contentTypeField));
      when(property.getData().getFieldByName(RestConfigurationUtils.HTTP_METHOD_FIELD_NAME)).thenReturn(
               Optional.of(httpMethodField));
      IPropertyDataValue addressValue = ConfigurationTestUtils.getMockedNetworkAddress(address);
      IPropertyDataValue interfaceValue = ConfigurationTestUtils.getMockedNetworkInterface(interfaceName);
      when(property.getData().getData(addressField)).thenReturn(addressValue);
      when(property.getData().getData(interfaceField)).thenReturn(interfaceValue);
      when(property.getData().getPrimitive(portField).getInteger()).thenReturn(BigInteger.valueOf(port));
      when(property.getData().getPrimitive(pathField).getString()).thenReturn(path);
      when(property.getData().getPrimitive(contentTypeField).getString()).thenReturn(contentType);
      when(property.getData().getEnumeration(httpMethodField).getValue()).thenReturn(method.toString());
      return property;
   }

   static IProperty getMockedZeroMqTcpConfiguration(ConnectionType type, String bind, String connection,
            int port) {
      IProperty property = mock(IProperty.class, RETURNS_DEEP_STUBS);
      when(property.getName()).thenReturn(UUID.randomUUID().toString());
      when(property.getCardinality()).thenReturn(FieldCardinality.SINGLE);
      when(property.getType()).thenReturn(DataTypes.DATA);
      when(property.getReferencedDataType().getFullyQualifiedName()).thenReturn(
               ZeroMqConfigurationUtils.ZERO_MQ_TCP_CONFIGURATION_QUALIFIED_NAME);
      IDataField connectionTypeField = mock(IDataField.class);
      IDataField bindField = mock(IDataField.class);
      IDataField bindInterfaceField = mock(IDataField.class);
      IDataField connectionField = mock(IDataField.class);
      IDataField connectAddressField = mock(IDataField.class);
      IDataField portField = mock(IDataField.class);
      when(property.getData().isSet()).thenReturn(true);
      when(property.getData().getFieldByName(ZeroMqConfigurationUtils.CONNECTION_TYPE_FIELD_NAME)).thenReturn(
               Optional.of(connectionTypeField));
      when(property.getData().getFieldByName(ZeroMqConfigurationUtils.BIND_CONFIGURATION_FIELD_NAME)).thenReturn(
               Optional.of(bindField));
      when(property.getData().getFieldByName(ZeroMqConfigurationUtils.CONNECT_CONFIGURATION_FIELD_NAME)).thenReturn(
               Optional.of(connectionField));
      when(property.getData().getFieldByName(RestConfigurationUtils.PORT_FIELD_NAME)).thenReturn(
               Optional.of(portField));
      when(property.getData().getData(bindField).getFieldByName(
               ZeroMqConfigurationUtils.BIND_CONFIGURATION_INTERFACE_FIELD_NAME))
                        .thenReturn(Optional.of(bindInterfaceField));
      when(property.getData().getData(connectionField).getFieldByName(
               ZeroMqConfigurationUtils.CONNECT_CONFIGURATION_REMOTE_ADDRESS_FIELD_NAME)).thenReturn(
                        Optional.of(connectAddressField));
      IPropertyDataValue bindInterface = ConfigurationTestUtils.getMockedNetworkInterface(bind);
      IPropertyDataValue connectAddress = ConfigurationTestUtils.getMockedNetworkAddress(connection);
      when(property.getData().getEnumeration(connectionTypeField).getValue()).thenReturn(type.name());
      when(property.getData().getData(bindField).getData(bindInterfaceField)).thenReturn(bindInterface);
      when(property.getData().getData(connectionField).getData(connectAddressField)).thenReturn(connectAddress);
      when(property.getData().getPrimitive(portField).getInteger()).thenReturn(BigInteger.valueOf(port));
      return property;
   }

   static IPropertyDataValue getMockedNetworkInterface(String name) {
      IPropertyDataValue value = mock(IPropertyDataValue.class, RETURNS_DEEP_STUBS);
      when(value.isSet()).thenReturn(true);
      when(value.isData()).thenReturn(true);
      when(value.getReferencedDataType().getFullyQualifiedName()).thenReturn(
               CommonConfigurationUtils.NETWORK_INTERFACE_QUALIFIED_NAME);
      IDataField nameField = mock(IDataField.class);
      when(value.getFieldByName(CommonConfigurationUtils.NETWORK_INTERFACE_NAME_FIELD_NAME)).thenReturn(
               Optional.of(nameField));
      when(value.getPrimitive(nameField).getString()).thenReturn(name);
      return value;
   }

   static IPropertyDataValue getMockedNetworkAddress(String name) {
      IPropertyDataValue value = mock(IPropertyDataValue.class, RETURNS_DEEP_STUBS);
      when(value.isSet()).thenReturn(true);
      when(value.isData()).thenReturn(true);
      when(value.getReferencedDataType().getFullyQualifiedName()).thenReturn(
               CommonConfigurationUtils.NETWORK_ADDRESS_QUALIFIED_NAME);
      IDataField nameField = mock(IDataField.class);
      when(value.getFieldByName(CommonConfigurationUtils.NETWORK_ADDRESS_ADDRESS_FIELD_NAME)).thenReturn(
               Optional.of(nameField));
      when(value.getPrimitive(nameField).getString()).thenReturn(name);
      return value;
   }

   static IModelReferenceField getMockedReference(String name, IModel type, IProperty... properties) {
      IModelReferenceField field = mock(IModelReferenceField.class, RETURNS_DEEP_STUBS);
      when(field.getName()).thenReturn(name);
      when(field.getType()).thenReturn(type);
      when(field.getProperties().iterator()).thenReturn(Arrays.asList(properties).iterator());
      when(field.getProperties().stream()).thenAnswer(args -> Stream.of(properties));
      return field;
   }

}
