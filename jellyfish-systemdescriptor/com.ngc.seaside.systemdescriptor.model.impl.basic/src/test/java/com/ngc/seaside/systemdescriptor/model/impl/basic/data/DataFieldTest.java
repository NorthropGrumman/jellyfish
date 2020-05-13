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
