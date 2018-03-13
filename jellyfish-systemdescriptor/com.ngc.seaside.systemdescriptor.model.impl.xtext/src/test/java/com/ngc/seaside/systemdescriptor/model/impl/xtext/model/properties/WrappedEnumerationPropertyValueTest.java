package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.properties;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.EnumerationValueDeclaration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class WrappedEnumerationPropertyValueTest extends AbstractWrappedXtextTest {
   private final static String PROPERTY_ENUM_VALUE = "ENUM_VALUE";

   private WrappedEnumerationPropertyValue setWrappedEnumValue;
   private WrappedEnumerationPropertyValue unsetWrappedEnumValue;

   @Mock
   private EnumerationValueDeclaration setDeclaration;

   @Mock
   private EnumerationValueDeclaration unsetDeclaration;

   @Before
   public void setup() {
      unsetWrappedEnumValue = new WrappedEnumerationPropertyValue(resolver(), unsetDeclaration);

      setDeclaration.setValue(PROPERTY_ENUM_VALUE);
      setWrappedEnumValue = new WrappedEnumerationPropertyValue(resolver(), setDeclaration);

      when(setDeclaration.getValue()).thenReturn(PROPERTY_ENUM_VALUE);
   }

   @Test
   public void testDoesReturnCorrectPropertyValueType() {
      assertEquals("enumeration property type is incorrect!", DataTypes.ENUM, unsetWrappedEnumValue.getType());
   }

   @Test
   public void testDoesReturnCorrectValue() {
      assertEquals("enumeration property value is incorrect!", PROPERTY_ENUM_VALUE, setWrappedEnumValue.getValue());
   }

   @Test
   public void testDoesCorrectlyDetermineIfValueIsSet() {
      assertFalse("enumeration property value is set but shouldn't be!", unsetWrappedEnumValue.isSet());
      assertTrue("enumeration property value is not set but should be!", setWrappedEnumValue.isSet());
   }
}
