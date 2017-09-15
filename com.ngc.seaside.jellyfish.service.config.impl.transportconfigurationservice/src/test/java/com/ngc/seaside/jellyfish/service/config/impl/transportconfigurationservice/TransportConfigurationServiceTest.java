package com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.jellyfish.service.scenario.api.IMessagingFlow;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class TransportConfigurationServiceTest {

   private TransportConfigurationService service;

   @Mock
   private IMessagingFlow flow;

   @Before
   public void setup() {
      service = new TransportConfigurationService();
      service.setLogService(new PrintStreamLogService());
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

}
