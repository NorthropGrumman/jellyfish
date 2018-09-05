/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice;

import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.config.api.dto.HttpMethod;
import com.ngc.seaside.jellyfish.service.config.api.dto.MulticastConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.zeromq.ConnectionType;
import com.ngc.seaside.jellyfish.service.config.api.dto.zeromq.ZeroMqConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.zeromq.ZeroMqTcpTransportConfiguration;
import com.ngc.seaside.jellyfish.service.scenario.api.IMessagingFlow;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import static com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice.ConfigurationTestUtils.getMockedLink;
import static com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice.ConfigurationTestUtils.getMockedMulticastConfiguration;
import static com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice.ConfigurationTestUtils.getMockedRestConfiguration;
import static com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice.ConfigurationTestUtils.getMockedZeroMqTcpConfiguration;
import static org.junit.Assert.assertEquals;
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
      IProperty property1 = getMockedRestConfiguration(address, interfaceName1, port1, path1,
               contentType, method);
      IModelLink<IDataReferenceField> link1 = getMockedLink(field, true, property1);

      IProperty property2 = getMockedRestConfiguration(address, interfaceName2, port2, path2,
               contentType, method);
      IModelLink<IDataReferenceField> link2 = getMockedLink(field, true, property2);

      IProperty property3 = getMockedRestConfiguration(address, interfaceName2, port2, path2,
               contentType, method);
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

   interface DataReferenceFieldLink extends IModelLink<IDataReferenceField> {

   }

}
