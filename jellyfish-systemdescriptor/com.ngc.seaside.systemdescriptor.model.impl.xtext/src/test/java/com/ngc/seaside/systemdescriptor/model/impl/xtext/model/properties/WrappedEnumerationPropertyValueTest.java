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
