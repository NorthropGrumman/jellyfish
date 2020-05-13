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

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.EnumPropertyValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Enumeration;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class WrappedEnumerationPropertyValueTest extends AbstractWrappedXtextTest {

   private static final String PROPERTY_ENUM_VALUE = "ENUM_VALUE";

   private WrappedEnumerationPropertyValue setWrappedEnumValue;
   private WrappedEnumerationPropertyValue unsetWrappedEnumValue;

   @Before
   public void setup() {
      Enumeration enumeration = factory().createEnumeration();
      when(resolver().getWrapperFor(enumeration)).thenReturn(
            new com.ngc.seaside.systemdescriptor.model.impl.basic.data.Enumeration("Enumeration"));

      EnumPropertyValue unsetDeclaration = factory().createEnumPropertyValue();
      unsetWrappedEnumValue = new WrappedEnumerationPropertyValue(resolver(), unsetDeclaration);

      EnumPropertyValue setDeclaration = factory().createEnumPropertyValue();
      setDeclaration.setValue(PROPERTY_ENUM_VALUE);
      setDeclaration.setEnumeration(enumeration);
      setWrappedEnumValue = new WrappedEnumerationPropertyValue(resolver(), setDeclaration);
   }

   @Test
   public void testDoesReturnCorrectPropertyValueType() {
      assertEquals("enumeration property type is incorrect!", DataTypes.ENUM, unsetWrappedEnumValue.getType());
      assertTrue("enumeration property value is not set but should be!", setWrappedEnumValue.isSet());
   }

   @Test
   public void testDoesReturnCorrectEnumeration() {
      assertEquals("referenced enumeration is incorrect!", "Enumeration",
                   setWrappedEnumValue.getReferencedEnumeration().getName());
      assertTrue("enumeration property value is not set but should be!", setWrappedEnumValue.isSet());
   }

   @Test
   public void testDoesReturnCorrectValue() {
      assertEquals("enumeration property value is incorrect!", PROPERTY_ENUM_VALUE, setWrappedEnumValue.getValue());
      assertTrue("enumeration property value is not set but should be!", setWrappedEnumValue.isSet());
   }
}
