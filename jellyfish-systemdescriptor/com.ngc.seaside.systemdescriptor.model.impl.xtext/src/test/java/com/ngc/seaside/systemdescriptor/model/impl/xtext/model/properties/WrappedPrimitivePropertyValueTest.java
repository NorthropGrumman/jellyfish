package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.properties;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.BooleanValue;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WrappedPrimitivePropertyValueTest extends AbstractWrappedXtextTest {
   private final static BooleanValue PROPERTY_BOOLEAN_VALUE = factory().createBooleanValue();

   @Before
   public void setup() {
      PROPERTY_BOOLEAN_VALUE.setValue("true");
   }

   @Test
   public void testDoesReturnCorrectType() {
      WrappedPrimitivePropertyValue value = new WrappedPrimitivePropertyValue(resolver(), PROPERTY_BOOLEAN_VALUE);
      assertEquals("boolean property type is incorrect!", value.getType(), DataTypes.BOOLEAN);
   }
}
