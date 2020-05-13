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
package com.ngc.seaside.systemdescriptor.model.impl.xtext.data;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Cardinality;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedDataModelFieldDeclaration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WrappedReferencedDataFieldTest extends AbstractWrappedXtextTest {

   private WrappedReferencedDataField wrappedDataField;

   private ReferencedDataModelFieldDeclaration field;

   private Data referencedData;

   @Mock
   private IData referenced;

   @Mock
   private IData parent;

   @Mock
   private IPackage pack;

   @Before
   public void setup() throws Throwable {
      Data parentData = factory().createData();
      parentData.setName("Foo");

      referencedData = factory().createData();
      referencedData.setName("referencedData");

      Package packageZ = factory().createPackage();
      packageZ.setName("my.foo.data");
      packageZ.setElement(referencedData);

      field = factory().createReferencedDataModelFieldDeclaration();
      field.setName("field1");
      field.setDataModel(referencedData);
      field.setCardinality(Cardinality.DEFAULT);
      parentData.getFields().add(field);

      when(referenced.getName()).thenReturn(referencedData.getName());
      when(referenced.getParent()).thenReturn(pack);
      when(pack.getName()).thenReturn(packageZ.getName());

      when(resolver().getWrapperFor(parentData)).thenReturn(parent);
      when(resolver().getWrapperFor(referencedData)).thenReturn(referenced);
      when(resolver().findXTextData(referenced.getName(), packageZ.getName())).thenReturn(Optional.of(referencedData));
   }

   @Test
   public void testDoesWrapXtextObject() throws Throwable {
      wrappedDataField = new WrappedReferencedDataField(resolver(), field);
      assertEquals("name not correct!",
                   wrappedDataField.getName(),
                   field.getName());
      assertEquals("parent not correct!",
                   parent,
                   wrappedDataField.getParent());
      assertEquals("metadata not set!",
                   IMetadata.EMPTY_METADATA,
                   wrappedDataField.getMetadata());
      assertEquals("referenced data not correct!",
                   referenced,
                   wrappedDataField.getReferencedDataType());
      assertEquals("cardinality not correct!",
                   FieldCardinality.SINGLE,
                   wrappedDataField.getCardinality());
   }

   @Test
   public void testDoesUpdateXtextObject() throws Throwable {
      Data anotherReferencedData = factory().createData();
      anotherReferencedData.setName("referencedData2");

      Package packageZ = factory().createPackage();
      packageZ.setName("more.of.foo.data");
      packageZ.setElement(anotherReferencedData);

      IPackage anotherPackage = mock(IPackage.class);
      when(anotherPackage.getName()).thenReturn(packageZ.getName());
      IData anotherReference = mock(IData.class);
      when(anotherReference.getName()).thenReturn(anotherReferencedData.getName());
      when(anotherReference.getParent()).thenReturn(anotherPackage);

      when(resolver().findXTextData(anotherReference.getName(), packageZ.getName()))
            .thenReturn(Optional.of(anotherReferencedData));

      wrappedDataField = new WrappedReferencedDataField(resolver(), field);
      // Should not throw an error.
      wrappedDataField.setType(DataTypes.DATA);
      wrappedDataField.setReferencedDataType(anotherReference);
      assertEquals("data not correct!",
                   anotherReferencedData,
                   field.getDataModel());

      wrappedDataField.setCardinality(FieldCardinality.MANY);
      assertEquals("cardinality not correct!",
                   FieldCardinality.MANY,
                   wrappedDataField.getCardinality());
   }

   @Test
   public void testDoesCreateXtextObject() throws Throwable {
      IDataField newField = mock(IDataField.class);
      when(newField.getName()).thenReturn("newField");
      when(newField.getType()).thenReturn(DataTypes.DATA);
      when(newField.getReferencedDataType()).thenReturn(referenced);
      when(newField.getCardinality()).thenReturn(FieldCardinality.MANY);

      ReferencedDataModelFieldDeclaration xtext = WrappedReferencedDataField.toXtext(resolver(), newField);
      assertEquals("name not correct!",
                   newField.getName(),
                   xtext.getName());
      assertEquals("referenced data not correct!",
                   referencedData,
                   xtext.getDataModel());
      assertEquals("cardinality not correct!",
                   xtext.getCardinality(),
                   Cardinality.MANY);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testNotAllowDataTypeToBeChangedToPrimitiveType() throws Throwable {
      wrappedDataField = new WrappedReferencedDataField(resolver(), field);
      wrappedDataField.setType(DataTypes.INT);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testNotAllowDataTypeToBeChangedToEnumType() throws Throwable {
      wrappedDataField = new WrappedReferencedDataField(resolver(), field);
      wrappedDataField.setType(DataTypes.ENUM);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testDoesNotCreateXtextObjectForPrimitiveType() throws Throwable {
      IDataField newField = mock(IDataField.class);
      when(newField.getType()).thenReturn(DataTypes.INT);
      WrappedReferencedDataField.toXtext(resolver(), newField);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testDoesNotCreateXtextObjectForEnumType() throws Throwable {
      IDataField newField = mock(IDataField.class);
      when(newField.getType()).thenReturn(DataTypes.ENUM);
      WrappedReferencedDataField.toXtext(resolver(), newField);
   }
}
