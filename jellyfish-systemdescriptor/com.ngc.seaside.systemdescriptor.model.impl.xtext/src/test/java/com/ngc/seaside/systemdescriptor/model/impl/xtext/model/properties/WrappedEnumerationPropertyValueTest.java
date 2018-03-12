package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.properties;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.EnumerationValueDeclaration;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

public class WrappedEnumerationPropertyValueTest extends AbstractWrappedXtextTest {
   private final static String PROPERTY_ENUM_VALUE = "ENUM_VALUE";

   @Mock
   private EnumerationValueDeclaration declaration;

   @Test
   public void testDoesReturnCorrectPropertyValueType() {
      WrappedEnumerationPropertyValue value = new WrappedEnumerationPropertyValue(resolver(), declaration);
      assertEquals("enumeration property type is incorrect!", DataTypes.ENUM, value.getType());
   }

   @Test
   public void testDoesCorrectlyDetermineIfValueIsSet() {
      WrappedEnumerationPropertyValue value = new WrappedEnumerationPropertyValue(resolver(), declaration);
      assertFalse("value is set but shouldn't be!", value.isSet());
      declaration.setValue(PROPERTY_ENUM_VALUE);
      value = new WrappedEnumerationPropertyValue(resolver(), declaration);
      assertTrue("value is not set but should be!", value.isSet());
   }
}
