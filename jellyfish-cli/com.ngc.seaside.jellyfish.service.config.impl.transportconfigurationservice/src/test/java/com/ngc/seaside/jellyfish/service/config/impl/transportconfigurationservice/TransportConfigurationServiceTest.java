package com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.config.api.dto.MulticastConfiguration;
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
      String address = "localhost";
      int port1 = 8080;
      int port2 = 8081;

      IDataReferenceField field = mock(IDataReferenceField.class);
      when(sdService.getAggregatedView(deploymentModel)).thenReturn(deploymentModel);

      IJellyFishCommandOptions options = mock(IJellyFishCommandOptions.class, RETURNS_DEEP_STUBS);
      when(options.getParameters()
                  .getParameter(CommonParameters.DEPLOYMENT_MODEL.getName())
                  .getStringValue()).thenReturn(deploymentModelName);
      when(options.getSystemDescriptor().findModel(deploymentModelName)).thenReturn(Optional.of(deploymentModel));
      IProperty property1 = getMockedMulticastConfiguration(address, port1);
      IModelLink<IDataReferenceField> link1 = getMockedLink(field, true, property1);

      IProperty property2 = getMockedMulticastConfiguration(address, port2);
      IModelLink<IDataReferenceField> link2 = getMockedLink(field, true, property2);

      IProperty property3 = getMockedMulticastConfiguration(address, port2);
      IModelLink<IDataReferenceField> link3 = getMockedLink(field, true, property3);

      when(deploymentModel.getLinks()).thenReturn(Arrays.asList(link1, link2, link3));

      Collection<MulticastConfiguration> configurations = service.getMulticastConfiguration(options, field);
      assertEquals(2, configurations.size());
      Iterator<MulticastConfiguration> iterator = configurations.iterator();
      MulticastConfiguration configuration1 = iterator.next();
      assertEquals(address, configuration1.getAddress());
      assertEquals(port1, configuration1.getPort());
      MulticastConfiguration configuration2 = iterator.next();
      assertEquals(address, configuration2.getAddress());
      assertEquals(port2, configuration2.getPort());
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

   private static IProperty getMockedMulticastConfiguration(String address, int port) {
      IProperty property = mock(IProperty.class, RETURNS_DEEP_STUBS);
      when(property.getName()).thenReturn(UUID.randomUUID().toString());
      when(property.getCardinality()).thenReturn(FieldCardinality.SINGLE);
      when(property.getType()).thenReturn(DataTypes.DATA);
      when(property.getReferencedDataType().getFullyQualifiedName()).thenReturn(
         TransportConfigurationService.MULTICAST_CONFIGURATION_QUALIFIED_NAME);
      IPropertyDataValue socketValue = mock(IPropertyDataValue.class, RETURNS_DEEP_STUBS);
      IDataField field = mock(IDataField.class);
      IDataField addressField = mock(IDataField.class);
      IDataField portField = mock(IDataField.class);
      when(property.getData().isSet()).thenReturn(true);
      when(property.getData().getFieldByName(TransportConfigurationService.SOCKET_ADDRESS_FIELD_NAME)).thenReturn(
         Optional.of(field));
      when(property.getData().getData(field)).thenReturn(socketValue);
      when(socketValue.getFieldByName(TransportConfigurationService.ADDRESS_FIELD_NAME)).thenReturn(
         Optional.of(addressField));
      when(socketValue.getFieldByName(TransportConfigurationService.PORT_FIELD_NAME)).thenReturn(
         Optional.of(portField));
      when(socketValue.getPrimitive(addressField).getString()).thenReturn(address);
      when(socketValue.getPrimitive(portField).getInteger()).thenReturn(BigInteger.valueOf(port));
      return property;
   }

   private interface DataReferenceFieldLink extends IModelLink<IDataReferenceField> {
   }

}
