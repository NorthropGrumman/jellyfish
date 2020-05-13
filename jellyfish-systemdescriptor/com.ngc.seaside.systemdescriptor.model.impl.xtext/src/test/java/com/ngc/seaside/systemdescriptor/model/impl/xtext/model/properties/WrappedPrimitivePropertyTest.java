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
package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.properties;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyValues;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Cardinality;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataType;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitivePropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Properties;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WrappedPrimitivePropertyTest extends AbstractWrappedXtextTest {

   private WrappedPrimitiveProperty wrappedProperty;

   private PrimitivePropertyFieldDeclaration property;

   @Mock
   private IProperties parent;

   @Before
   public void setup() throws Throwable {
      Properties parentData = factory().createProperties();

      property = factory().createPrimitivePropertyFieldDeclaration();
      property.setName("property1");
      property.setType(PrimitiveDataType.STRING);
      property.setCardinality(Cardinality.DEFAULT);
      parentData.getDeclarations().add(property);

      when(resolver().getWrapperFor(parentData)).thenReturn(parent);
   }

   @Test
   public void testDoesWrapXtextObject() throws Throwable {
      wrappedProperty = new WrappedPrimitiveProperty(resolver(),
                                                     property,
                                                     IPropertyValues.emptyPropertyValues());
      assertEquals("name not correct!",
                   wrappedProperty.getName(),
                   property.getName());
      assertEquals("parent not correct!",
                   parent,
                   wrappedProperty.getParent());
      assertEquals("cardinality not correct!",
                   FieldCardinality.SINGLE,
                   wrappedProperty.getCardinality());
   }

   @Test
   public void testDoesCreateXtextObject() throws Throwable {
      IProperty newProperty = mock(IProperty.class);
      when(newProperty.getName()).thenReturn("newField");
      when(newProperty.getType()).thenReturn(DataTypes.INT);
      when(newProperty.getCardinality()).thenReturn(FieldCardinality.MANY);

      PrimitivePropertyFieldDeclaration xtext = WrappedPrimitiveProperty.toXtextPrimitivePropertyFieldDeclaration(
            resolver(),
            newProperty);
      assertEquals("name not correct!",
                   newProperty.getName(),
                   xtext.getName());
      assertEquals("type not correct!",
                   PrimitiveDataType.INT,
                   xtext.getType());
      assertEquals("cardinality not correct!",
                   xtext.getCardinality(),
                   Cardinality.MANY);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testDoesNotCreateXtextObjectForNonPrimitiveType() throws Throwable {
      IProperty newProperty = mock(IProperty.class);
      when(newProperty.getType()).thenReturn(DataTypes.DATA);
      WrappedPrimitiveProperty.toXtextPrimitivePropertyFieldDeclaration(resolver(), newProperty);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testDoesNotCreateXtextObjectForNonPrimitiveType2() throws Throwable {
      IProperty newField = mock(IProperty.class);
      when(newField.getType()).thenReturn(DataTypes.ENUM);
      WrappedPrimitiveProperty.toXtextPrimitivePropertyFieldDeclaration(resolver(), newField);
   }
}
