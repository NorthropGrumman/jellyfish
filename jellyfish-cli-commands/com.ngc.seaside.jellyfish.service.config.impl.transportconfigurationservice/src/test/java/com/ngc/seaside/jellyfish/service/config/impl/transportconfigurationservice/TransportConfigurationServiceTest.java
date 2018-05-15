package com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice;

import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.config.api.dto.HttpMethod;
import com.ngc.seaside.jellyfish.service.config.api.dto.MulticastConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.RestTelemetryConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.TelemetryConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.zeromq.ConnectionType;
import com.ngc.seaside.jellyfish.service.config.api.dto.zeromq.ZeroMqConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.zeromq.ZeroMqTcpTransportConfiguration;
import com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice.utils.CommonConfigurationUtils;
import com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice.utils.MulticastConfigurationUtils;
import com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice.utils.RestConfigurationUtils;
import com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice.utils.TelemetryConfigurationUtils;
import com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice.utils.ZeroMqConfigurationUtils;
import com.ngc.seaside.jellyfish.service.scenario.api.IMessagingFlow;
import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.test.systemdescriptor.ModelUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TransportConfigurationServiceTest {

   private TransportConfigurationService service;

   @Mock
   private IMessagingFlow flow;

   @Mock
   private ISystemDescriptorService sdService;

   @Before
   public void setup() {
      service = new TransportConfigurationService();
      service.setLogService(new PrintStreamLogService());
      service.setSystemDescriptorService(sdService);
   }

   @Test
   public void test() {
      String[][] tests = {
            {"TEST1_DATA_OBJECT123", "Test1DataObject123"},
            {"TEST_DATA_OBJ1ECT", "testDataObj1ect"},
            {"TEST_XML_OBJECT", "TestXMLObject"},
            {"XML_OBJECT_XML", "XMLObjectXML"},
            {"TEST_DATA_OBJECT", "Test_data_Object"},
            };
      for (String[] test : tests) {
         final String expected = test[0];
         IDataReferenceField field = mock(IDataReferenceField.class);
         when(field.getType()).thenReturn(mock(IData.class));
         when(field.getType().getName()).thenReturn(test[1]);
         assertEquals(test[1], expected, service.getTransportTopicName(flow, field));
      }

   }

   @Test
   public void testMulticastConfiguration() {
      String deploymentModelName = "com.ngc.DeploymentModel";
      IModel deploymentModel = mock(IModel.class, RETURNS_DEEP_STUBS);
      String groupAddress = "224.5.6.7";
      String sourceAndTargetInterfaceName = "127.0.0.1";

      int port1 = 8080;
      int port2 = 8081;

      IDataReferenceField field = mock(IDataReferenceField.class);
      when(sdService.getAggregatedView(deploymentModel)).thenReturn(deploymentModel);

      IJellyFishCommandOptions options = mock(IJellyFishCommandOptions.class, RETURNS_DEEP_STUBS);
      when(options.getParameters()
                 .getParameter(CommonParameters.DEPLOYMENT_MODEL.getName())
                 .getStringValue()).thenReturn(deploymentModelName);
      when(options.getSystemDescriptor().findModel(deploymentModelName)).thenReturn(Optional.of(deploymentModel));
      IProperty property1 = getMockedMulticastConfiguration(groupAddress, port1, sourceAndTargetInterfaceName,
                                                            sourceAndTargetInterfaceName);
      IModelLink<IDataReferenceField> link1 = getMockedLink(field, true, property1);

      IProperty property2 = getMockedMulticastConfiguration(groupAddress, port2, sourceAndTargetInterfaceName,
                                                            sourceAndTargetInterfaceName);
      IModelLink<IDataReferenceField> link2 = getMockedLink(field, true, property2);

      IProperty property3 = getMockedMulticastConfiguration(groupAddress, port2, sourceAndTargetInterfaceName,
                                                            sourceAndTargetInterfaceName);
      IModelLink<IDataReferenceField> link3 = getMockedLink(field, true, property3);

      when(deploymentModel.getLinks()).thenReturn(Arrays.asList(link1, link2, link3));

      Collection<MulticastConfiguration> configurations = service.getMulticastConfiguration(options, field);
      assertEquals(2, configurations.size());
      Iterator<MulticastConfiguration> iterator = configurations.iterator();
      MulticastConfiguration configuration1 = iterator.next();
      assertEquals(groupAddress, configuration1.getGroupAddress());
      assertEquals(port1, configuration1.getPort());
      assertEquals(sourceAndTargetInterfaceName, configuration1.getSourceInterface().getName());
      assertEquals(sourceAndTargetInterfaceName, configuration1.getTargetInterface().getName());
      MulticastConfiguration configuration2 = iterator.next();
      assertEquals(groupAddress, configuration2.getGroupAddress());
      assertEquals(port2, configuration2.getPort());
      assertEquals(sourceAndTargetInterfaceName, configuration2.getSourceInterface().getName());
      assertEquals(sourceAndTargetInterfaceName, configuration2.getTargetInterface().getName());
   }

   @Test
   public void testRestConfiguration() {
      String deploymentModelName = "com.ngc.DeploymentModel";
      IModel deploymentModel = mock(IModel.class, RETURNS_DEEP_STUBS);
      String address = "localhost";
      String interfaceName1 = "0.0.0.0";
      String interfaceName2 = "eth0";
      int port1 = 8080;
      int port2 = 8081;
      String path1 = "/path1";
      String path2 = "/path2";
      String contentType = "application/x-protobuf";
      HttpMethod method = HttpMethod.POST;

      IDataReferenceField field = mock(IDataReferenceField.class);
      when(sdService.getAggregatedView(deploymentModel)).thenReturn(deploymentModel);

      IJellyFishCommandOptions options = mock(IJellyFishCommandOptions.class, RETURNS_DEEP_STUBS);
      when(options.getParameters()
                 .getParameter(CommonParameters.DEPLOYMENT_MODEL.getName())
                 .getStringValue()).thenReturn(deploymentModelName);
      when(options.getSystemDescriptor().findModel(deploymentModelName)).thenReturn(Optional.of(deploymentModel));
      IProperty property1 = getMockedRestConfiguration(address, interfaceName1, port1, path1, contentType, method);
      IModelLink<IDataReferenceField> link1 = getMockedLink(field, true, property1);

      IProperty property2 = getMockedRestConfiguration(address, interfaceName2, port2, path2, contentType, method);
      IModelLink<IDataReferenceField> link2 = getMockedLink(field, true, property2);

      IProperty property3 = getMockedRestConfiguration(address, interfaceName2, port2, path2, contentType, method);
      IModelLink<IDataReferenceField> link3 = getMockedLink(field, true, property3);

      when(deploymentModel.getLinks()).thenReturn(Arrays.asList(link1, link2, link3));

      Collection<RestConfiguration> configurations = service.getRestConfiguration(options, field);
      assertEquals(2, configurations.size());
      Iterator<RestConfiguration> iterator = configurations.iterator();
      RestConfiguration configuration1 = iterator.next();
      assertEquals(address, configuration1.getNetworkAddress().getAddress());
      assertEquals(interfaceName1, configuration1.getNetworkInterface().getName());
      assertEquals(port1, configuration1.getPort());
      assertEquals(path1, configuration1.getPath());
      assertEquals(contentType, configuration1.getContentType());
      assertEquals(method, configuration1.getHttpMethod());
      RestConfiguration configuration2 = iterator.next();
      assertEquals(address, configuration2.getNetworkAddress().getAddress());
      assertEquals(interfaceName2, configuration2.getNetworkInterface().getName());
      assertEquals(port2, configuration2.getPort());
      assertEquals(path2, configuration2.getPath());
      assertEquals(contentType, configuration2.getContentType());
      assertEquals(method, configuration2.getHttpMethod());
   }

   @Test
   public void testZeroMqConfiguration() {
      String deploymentModelName = "com.ngc.DeploymentModel";
      IModel deploymentModel = mock(IModel.class, RETURNS_DEEP_STUBS);
      String bindInterface = "127.0.0.1";
      String connectAddress = "1.2.3.4";

      int port1 = 8080;
      int port2 = 8081;

      IDataReferenceField field = mock(IDataReferenceField.class);
      when(sdService.getAggregatedView(deploymentModel)).thenReturn(deploymentModel);

      IJellyFishCommandOptions options = mock(IJellyFishCommandOptions.class, RETURNS_DEEP_STUBS);
      when(options.getParameters()
                 .getParameter(CommonParameters.DEPLOYMENT_MODEL.getName())
                 .getStringValue()).thenReturn(deploymentModelName);
      when(options.getSystemDescriptor().findModel(deploymentModelName)).thenReturn(Optional.of(deploymentModel));
      IProperty property1 = getMockedZeroMqTcpConfiguration(ConnectionType.SOURCE_BINDS_TARGET_CONNECTS,
                                                            bindInterface,
                                                            connectAddress,
                                                            port1);
      IModelLink<IDataReferenceField> link1 = getMockedLink(field, true, property1);

      IProperty property2 = getMockedZeroMqTcpConfiguration(ConnectionType.SOURCE_BINDS_TARGET_CONNECTS,
                                                            bindInterface,
                                                            connectAddress,
                                                            port2);
      IModelLink<IDataReferenceField> link2 = getMockedLink(field, true, property2);

      IProperty property3 = getMockedZeroMqTcpConfiguration(ConnectionType.SOURCE_BINDS_TARGET_CONNECTS,
                                                            bindInterface,
                                                            connectAddress,
                                                            port2);
      IModelLink<IDataReferenceField> link3 = getMockedLink(field, true, property3);

      when(deploymentModel.getLinks()).thenReturn(Arrays.asList(link1, link2, link3));

      Collection<ZeroMqConfiguration> configurations = service.getZeroMqConfiguration(options, field);
      assertEquals(2, configurations.size());
      Iterator<ZeroMqConfiguration> iterator = configurations.iterator();
      ZeroMqConfiguration configuration1 = iterator.next();
      assertEquals(ZeroMqTcpTransportConfiguration.class, configuration1.getClass());
      assertEquals(bindInterface, ((ZeroMqTcpTransportConfiguration) configuration1).getBindConfiguration().getName());
      assertEquals(connectAddress,
                   ((ZeroMqTcpTransportConfiguration) configuration1).getConnectConfiguration().getAddress());
      assertEquals(port1, ((ZeroMqTcpTransportConfiguration) configuration1).getPort());
      ZeroMqConfiguration configuration2 = iterator.next();
      assertEquals(ZeroMqTcpTransportConfiguration.class, configuration2.getClass());
      assertEquals(bindInterface, ((ZeroMqTcpTransportConfiguration) configuration2).getBindConfiguration().getName());
      assertEquals(connectAddress,
                   ((ZeroMqTcpTransportConfiguration) configuration2).getConnectConfiguration().getAddress());
      assertEquals(port2, ((ZeroMqTcpTransportConfiguration) configuration2).getPort());
   }

   @Test
   public void testTelemetryConfiguration() {
      String deploymentModelName = "com.ngc.DeploymentModel";
      IModel deploymentModel = mock(IModel.class, RETURNS_DEEP_STUBS);
      String modelName = "com.ngc.Model";
      IModel model = mock(IModel.class, RETURNS_DEEP_STUBS);
      String address = "localhost";
      String interfaceName = "0.0.0.0";
      int port = 8081;
      String path = "/path2";
      String contentType = "application/x-protobuf";
      HttpMethod method = HttpMethod.POST;

      IProperty configProperty = getMockedRestTelemetryConfiguration(address,
         interfaceName,
         port,
         path,
         contentType,
         method);

      IJellyFishCommandOptions options = mock(IJellyFishCommandOptions.class, RETURNS_DEEP_STUBS);
      when(options.getParameters()
                 .getParameter(CommonParameters.DEPLOYMENT_MODEL.getName())
                 .getStringValue()).thenReturn(deploymentModelName);
      when(options.getSystemDescriptor().findModel(deploymentModelName)).thenReturn(Optional.of(deploymentModel));
      when(options.getSystemDescriptor().findModel(modelName)).thenReturn(Optional.of(model));
      when(sdService.getAggregatedView(deploymentModel)).thenReturn(deploymentModel);
      when(sdService.getAggregatedView(model)).thenReturn(model);
      IModelReferenceField part = getMockedReference("model", model, configProperty);
      INamedChildCollection<IModel, IModelReferenceField> parts = ModelUtils.mockedNamedCollectionOf(part);
      when(deploymentModel.getParts()).thenReturn(parts);
      when(model.getProperties()).thenReturn(IProperties.EMPTY_PROPERTIES);
      
      Collection<TelemetryConfiguration> configurations = service.getTelemetryConfiguration(options, model);
      assertEquals(1, configurations.size());
      assertTrue(configurations.iterator().next() instanceof RestTelemetryConfiguration);
      RestTelemetryConfiguration config = (RestTelemetryConfiguration) configurations.iterator().next();
      RestConfiguration restConfig = config.getConfig();
      assertEquals(address, restConfig.getNetworkAddress().getAddress());
      assertEquals(interfaceName, restConfig.getNetworkInterface().getName());
      assertEquals(port, restConfig.getPort());
      assertEquals(path, restConfig.getPath());
      assertEquals(contentType, restConfig.getContentType());
      assertEquals(method, restConfig.getHttpMethod());
   }
   
   private static IModelReferenceField getMockedReference(String name, IModel type, IProperty... properties) {
      IModelReferenceField field = mock(IModelReferenceField.class, RETURNS_DEEP_STUBS);
      when(field.getName()).thenReturn(name);
      when(field.getType()).thenReturn(type);
      when(field.getProperties().iterator()).thenReturn(Arrays.asList(properties).iterator());
      when(field.getProperties().stream()).thenAnswer(args -> Stream.of(properties));
      return field;
   }
   
   private static IModelLink<IDataReferenceField> getMockedLink(IDataReferenceField field, boolean fieldIsSource,
                                                                IProperty... properties) {
      IModelLink<IDataReferenceField> link = mock(DataReferenceFieldLink.class, RETURNS_DEEP_STUBS);

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

   private static IProperty getMockedMulticastConfiguration(String groupAddress, int port, String sourceInterfaceName,
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
      IPropertyDataValue sourceInterface = getMockedNetworkInterface(sourceInterfaceName);
      IPropertyDataValue targetInterface = getMockedNetworkInterface(targetInterfaceName);
      when(property.getData().getData(sourceField)).thenReturn(sourceInterface);
      when(property.getData().getData(targetField)).thenReturn(targetInterface);
      return property;
   }

   private static IProperty getMockedRestTelemetryConfiguration(String address, String interfaceName, int port, 
                                                                String path, String contentType, HttpMethod method) {
      IProperty property = mock(IProperty.class, RETURNS_DEEP_STUBS);
      when(property.getName()).thenReturn(UUID.randomUUID().toString());
      when(property.getCardinality()).thenReturn(FieldCardinality.SINGLE);
      when(property.getType()).thenReturn(DataTypes.DATA);
      when(property.getReferencedDataType().getFullyQualifiedName()).thenReturn(
            TelemetryConfigurationUtils.REST_TELEMETRY_CONFIGURATION_QUALIFIED_NAME);
      IDataField configField = mock(IDataField.class);
      when(property.getData().isSet()).thenReturn(true);
      when(property.getData().getFieldByName(TelemetryConfigurationUtils.CONFIG_FIELD_NAME)).thenReturn(
            Optional.of(configField));
      IPropertyDataValue restConfig = getMockedRestConfiguration(address, interfaceName, port, path, contentType,
         method).getData();
      when(property.getData().getData(configField)).thenReturn(restConfig);
      return property;
   }
   
   private static IProperty getMockedRestConfiguration(String address, String interfaceName, int port, String path,
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
      IPropertyDataValue addressValue = getMockedNetworkAddress(address);
      IPropertyDataValue interfaceValue = getMockedNetworkInterface(interfaceName);
      when(property.getData().getData(addressField)).thenReturn(addressValue);
      when(property.getData().getData(interfaceField)).thenReturn(interfaceValue);
      when(property.getData().getPrimitive(portField).getInteger()).thenReturn(BigInteger.valueOf(port));
      when(property.getData().getPrimitive(pathField).getString()).thenReturn(path);
      when(property.getData().getPrimitive(contentTypeField).getString()).thenReturn(contentType);
      when(property.getData().getEnumeration(httpMethodField).getValue()).thenReturn(method.toString());
      return property;
   }

   private static IProperty getMockedZeroMqTcpConfiguration(ConnectionType type, String bind, String connection,
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
      IPropertyDataValue bindInterface = getMockedNetworkInterface(bind);
      IPropertyDataValue connectAddress = getMockedNetworkAddress(connection);
      when(property.getData().getEnumeration(connectionTypeField).getValue()).thenReturn(type.name());
      when(property.getData().getData(bindField).getData(bindInterfaceField)).thenReturn(bindInterface);
      when(property.getData().getData(connectionField).getData(connectAddressField)).thenReturn(connectAddress);
      when(property.getData().getPrimitive(portField).getInteger()).thenReturn(BigInteger.valueOf(port));
      return property;
   }

   private static IPropertyDataValue getMockedNetworkInterface(String name) {
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

   private static IPropertyDataValue getMockedNetworkAddress(String name) {
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

   private interface DataReferenceFieldLink extends IModelLink<IDataReferenceField> {

   }

}
