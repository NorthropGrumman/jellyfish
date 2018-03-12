package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.properties;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.BooleanValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DblValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.IntValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.StringValue;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WrappedPrimitivePropertyValueTest extends AbstractWrappedXtextTest {
   private final static BooleanValue PROPERTY_BOOLEAN_VALUE = factory().createBooleanValue();
   private final static IntValue PROPERTY_INTEGER_VALUE = factory().createIntValue();
   private final static DblValue PROPERTY_FLOAT_VALUE = factory().createDblValue();
   private final static StringValue PROPERTY_STRING_VALUE = factory().createStringValue();

   @Test
   public void testDoesReturnCorrectType() {
      WrappedPrimitivePropertyValue value = new WrappedPrimitivePropertyValue(resolver(), PROPERTY_BOOLEAN_VALUE);
      assertEquals("boolean property type is incorrect!", value.getType(), DataTypes.BOOLEAN);

      value = new WrappedPrimitivePropertyValue(resolver(), PROPERTY_INTEGER_VALUE);
      assertEquals("integer property type is incorrect!", value.getType(), DataTypes.INT);

      value = new WrappedPrimitivePropertyValue(resolver(), PROPERTY_FLOAT_VALUE);
      assertEquals("float property type is incorrect!", value.getType(), DataTypes.FLOAT);

      value = new WrappedPrimitivePropertyValue(resolver(), PROPERTY_STRING_VALUE);
      assertEquals("string property type is incorrect!", value.getType(), DataTypes.STRING);
   }
}
