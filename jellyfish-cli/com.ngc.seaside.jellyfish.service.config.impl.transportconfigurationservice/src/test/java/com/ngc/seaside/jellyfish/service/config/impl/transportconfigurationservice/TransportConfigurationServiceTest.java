package com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.config.api.dto.HttpMethod;
import com.ngc.seaside.jellyfish.service.config.api.dto.MulticastConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;
import com.ngc.seaside.jellyfish.service.scenario.api.IMessagingFlow;
import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;

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
               { "TEST1_DATA_OBJECT123", "Test1DataObject123" },
               { "TEST_DATA_OBJ1ECT", "testDataObj1ect" },
               { "TEST_XML_OBJECT", "TestXMLObject" },
               { "XML_OBJECT_XML", "XMLObjectXML" },
               { "TEST_DATA_OBJECT", "Test_data_Object" },
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
      String interfaceName1 = "*";
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
      when(link.getProperties().stream()).thenReturn(Stream.of(properties));
      return link;
   }

   private static IProperty getMockedMulticastConfiguration(String groupAddress, int port, String
         sourceInterfaceName, String targetInterfaceName) {
      IProperty property = mock(IProperty.class, RETURNS_DEEP_STUBS);
      when(property.getName()).thenReturn(UUID.randomUUID().toString());
      when(property.getCardinality()).thenReturn(FieldCardinality.SINGLE);
      when(property.getType()).thenReturn(DataTypes.DATA);
      when(property.getReferencedDataType().getFullyQualifiedName()).thenReturn(
         TransportConfigurationService.MULTICAST_CONFIGURATION_QUALIFIED_NAME);
      IPropertyDataValue socketValue = mock(IPropertyDataValue.class, RETURNS_DEEP_STUBS);
      IDataField field = mock(IDataField.class);
      IDataField groupAddressField = mock(IDataField.class);
      IDataField portField = mock(IDataField.class);
      IDataField sourceField = mock(IDataField.class);
      IDataField targetField = mock(IDataField.class);
      when(property.getData().isSet()).thenReturn(true);
      when(property.getData()
                   .getFieldByName(TransportConfigurationService.MULTICAST_SOCKET_ADDRESS_FIELD_NAME)).thenReturn(
                      Optional.of(field));
      when(property.getData().getData(field)).thenReturn(socketValue);
      when(socketValue.getFieldByName(TransportConfigurationService.GROUP_ADDRESS_FIELD_NAME)).thenReturn(
         Optional.of(groupAddressField));
      when(socketValue.getFieldByName(TransportConfigurationService.PORT_FIELD_NAME)).thenReturn(
         Optional.of(portField));
      when(socketValue.getFieldByName(TransportConfigurationService.SOURCE_ADDRESS_FIELD_NAME)).thenReturn(
            Optional.of(sourceField));
      when(socketValue.getFieldByName(TransportConfigurationService.TARGET_ADDRESS_FIELD_NAME)).thenReturn(
            Optional.of(targetField));
      when(socketValue.getPrimitive(groupAddressField).getString()).thenReturn(groupAddress);
      when(socketValue.getPrimitive(portField).getInteger()).thenReturn(BigInteger.valueOf(port));
      when(socketValue.getPrimitive(sourceField).getString()).thenReturn(sourceInterfaceName);
      when(socketValue.getPrimitive(targetField).getString()).thenReturn(targetInterfaceName);
      return property;
   }

   private IProperty getMockedRestConfiguration(String address, String interfaceName, int port, String path,
                                                String contentType,
                                                HttpMethod method) {
      IProperty property = mock(IProperty.class, RETURNS_DEEP_STUBS);
      when(property.getName()).thenReturn(UUID.randomUUID().toString());
      when(property.getCardinality()).thenReturn(FieldCardinality.SINGLE);
      when(property.getType()).thenReturn(DataTypes.DATA);
      when(property.getReferencedDataType().getFullyQualifiedName()).thenReturn(
         TransportConfigurationService.REST_CONFIGURATION_QUALIFIED_NAME);
      IPropertyDataValue socketValue = mock(IPropertyDataValue.class, RETURNS_DEEP_STUBS);
      IDataField socketField = mock(IDataField.class);
      IDataField addressField = mock(IDataField.class);
      IDataField networkInterfaceField = mock(IDataField.class);
      IDataField portField = mock(IDataField.class);
      IDataField pathField = mock(IDataField.class);
      IDataField contentTypeField = mock(IDataField.class);
      IDataField httpMethodField = mock(IDataField.class);
      when(property.getData().isSet()).thenReturn(true);
      when(property.getData().getFieldByName(TransportConfigurationService.REST_SOCKET_ADDRESS_FIELD_NAME)).thenReturn(
         Optional.of(socketField));
      when(property.getData().getFieldByName(TransportConfigurationService.REST_PATH_FIELD_NAME)).thenReturn(
         Optional.of(pathField));
      when(property.getData().getFieldByName(TransportConfigurationService.REST_CONTENT_TYPE_FIELD_NAME)).thenReturn(
         Optional.of(contentTypeField));
      when(property.getData().getFieldByName(TransportConfigurationService.REST_HTTP_METHOD_FIELD_NAME)).thenReturn(
         Optional.of(httpMethodField));
      when(property.getData().getData(socketField)).thenReturn(socketValue);
      when(socketValue.getFieldByName(TransportConfigurationService.ADDRESS_FIELD_NAME)).thenReturn(
            Optional.of(addressField));
      when(socketValue.getFieldByName(TransportConfigurationService.NETWORK_INTERFACE_FIELD_NAME)).thenReturn(
            Optional.of(networkInterfaceField));
      when(socketValue.getFieldByName(TransportConfigurationService.PORT_FIELD_NAME)).thenReturn(
         Optional.of(portField));
      when(socketValue.getPrimitive(addressField).getString()).thenReturn(address);
      when(socketValue.getPrimitive(networkInterfaceField).getString()).thenReturn(interfaceName);
      when(socketValue.getPrimitive(portField).getInteger()).thenReturn(BigInteger.valueOf(port));
      when(property.getData().getEnumeration(httpMethodField).getValue()).thenReturn(method.toString());
      when(property.getData().getPrimitive(pathField).getString()).thenReturn(path);
      when(property.getData().getPrimitive(contentTypeField).getString()).thenReturn(contentType);
      return property;
   }

   private interface DataReferenceFieldLink extends IModelLink<IDataReferenceField> {
   }

}
