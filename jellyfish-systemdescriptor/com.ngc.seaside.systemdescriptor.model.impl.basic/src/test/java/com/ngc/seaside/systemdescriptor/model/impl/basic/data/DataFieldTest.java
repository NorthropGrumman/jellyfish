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
package com.ngc.seaside.systemdescriptor.model.impl.basic.data;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.basic.metadata.Metadata;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.json.Json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class DataFieldTest {

   private DataField field;

   @Mock
   private IData parent;

   @Mock
   private IData referencedDataType;

   private IMetadata metadata;

   @Before
   public void setup() throws Throwable {
      metadata = new Metadata();
      metadata.setJson(Json.createObjectBuilder().add("foo", "bar").build());
   }

   @Test
   public void testDoesMakePrimitiveDataField() throws Throwable {
      field = new DataField("field1");
      field.setType(DataTypes.STRING);
      assertEquals("name not correct!",
                   "field1",
                   field.getName());
      assertEquals("type not correct!",
                   DataTypes.STRING,
                   field.getType());
      assertNull("referenceDataType should be null if field is primitive!",
                 field.getReferencedDataType());
   }

   @Test
   public void testDoesMakeDataReferencingDataField() throws Throwable {
      field = new DataField("field1");
      field.setType(DataTypes.DATA);
      field.setReferencedDataType(referencedDataType);
      assertEquals("name not correct!",
                   "field1",
                   field.getName());
      assertEquals("type not correct!",
                   DataTypes.DATA,
                   field.getType());
      assertEquals("referenceDataType not correct!!",
                   referencedDataType,
                   field.getReferencedDataType());
   }
}
