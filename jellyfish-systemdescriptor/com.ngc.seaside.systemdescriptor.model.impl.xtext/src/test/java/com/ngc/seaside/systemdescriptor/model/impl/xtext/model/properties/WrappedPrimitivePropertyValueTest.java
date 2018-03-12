package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.properties;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.BooleanValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DblValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.IntValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.StringValue;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.*;

public class WrappedPrimitivePropertyValueTest extends AbstractWrappedXtextTest {
   private final static BooleanValue PROPERTY_BOOLEAN_VALUE = factory().createBooleanValue();
   private final static IntValue PROPERTY_INTEGER_VALUE = factory().createIntValue();
   private final static DblValue PROPERTY_FLOAT_VALUE = factory().createDblValue();
   private final static StringValue PROPERTY_STRING_VALUE = factory().createStringValue();

   private WrappedPrimitivePropertyValue wrappedBooleanValue;
   private WrappedPrimitivePropertyValue wrappedIntegerValue;
   private WrappedPrimitivePropertyValue wrappedFloatValue;
   private WrappedPrimitivePropertyValue wrappedStringValue;

   @Before
   public void setup() {
      wrappedBooleanValue = new WrappedPrimitivePropertyValue(resolver(), PROPERTY_BOOLEAN_VALUE);
      wrappedIntegerValue = new WrappedPrimitivePropertyValue(resolver(), PROPERTY_INTEGER_VALUE);
      wrappedFloatValue = new WrappedPrimitivePropertyValue(resolver(), PROPERTY_FLOAT_VALUE);
      wrappedStringValue = new WrappedPrimitivePropertyValue(resolver(), PROPERTY_STRING_VALUE);
   }

   @Test
   public void testDoesReturnCorrectType() {
      assertEquals("boolean property type is incorrect!", wrappedBooleanValue.getType(), DataTypes.BOOLEAN);
      assertEquals("integer property type is incorrect!", wrappedIntegerValue.getType(), DataTypes.INT);
      assertEquals("float property type is incorrect!", wrappedFloatValue.getType(), DataTypes.FLOAT);
      assertEquals("string property type is incorrect!", wrappedStringValue.getType(), DataTypes.STRING);
   }

   @Test
   public void testDoesCorrectlyDetermineIfValueIsSet() {
      assertFalse("boolean value is set but shouldn't be", wrappedBooleanValue.isSet());
      PROPERTY_BOOLEAN_VALUE.setValue("true");
      wrappedBooleanValue = new WrappedPrimitivePropertyValue(resolver(), PROPERTY_BOOLEAN_VALUE);
      assertTrue("boolean value is not set but should be!", wrappedBooleanValue.isSet());
   }

   @Test
   public void testDoesCorrectlyRetrieveSetPropertyValue() {
      PROPERTY_BOOLEAN_VALUE.setValue("true");
      wrappedBooleanValue = new WrappedPrimitivePropertyValue(resolver(), PROPERTY_BOOLEAN_VALUE);
      assertTrue("boolean property value is incorrect!", wrappedBooleanValue.getBoolean());

      PROPERTY_BOOLEAN_VALUE.setValue("false");
      wrappedBooleanValue = new WrappedPrimitivePropertyValue(resolver(), PROPERTY_BOOLEAN_VALUE);
      assertFalse("boolean property value is incorrect!", wrappedBooleanValue.getBoolean());

      PROPERTY_INTEGER_VALUE.setValue(-1);
      wrappedIntegerValue = new WrappedPrimitivePropertyValue(resolver(), PROPERTY_INTEGER_VALUE);
      assertEquals("integer property value is incorrect!", new BigInteger("-1"), wrappedIntegerValue.getInteger());

      PROPERTY_INTEGER_VALUE.setValue(0);
      wrappedIntegerValue = new WrappedPrimitivePropertyValue(resolver(), PROPERTY_INTEGER_VALUE);
      assertEquals("integer property value is incorrect!", new BigInteger("0"), wrappedIntegerValue.getInteger());

      PROPERTY_INTEGER_VALUE.setValue(1);
      wrappedIntegerValue = new WrappedPrimitivePropertyValue(resolver(), PROPERTY_INTEGER_VALUE);
      assertEquals("integer property value is incorrect!", new BigInteger("1"), wrappedIntegerValue.getInteger());

      PROPERTY_FLOAT_VALUE.setValue(-1.0);
      wrappedFloatValue = new WrappedPrimitivePropertyValue(resolver(), PROPERTY_FLOAT_VALUE);
      assertEquals("float property value is incorrect!", new BigDecimal(-1.0), wrappedFloatValue.getDecimal());

      PROPERTY_FLOAT_VALUE.setValue(0.0);
      wrappedFloatValue = new WrappedPrimitivePropertyValue(resolver(), PROPERTY_FLOAT_VALUE);
      assertEquals("float property value is incorrect!", new BigDecimal(0.0), wrappedFloatValue.getDecimal());

      PROPERTY_FLOAT_VALUE.setValue(1.0);
      wrappedFloatValue = new WrappedPrimitivePropertyValue(resolver(), PROPERTY_FLOAT_VALUE);
      assertEquals("float property value is incorrect!", new BigDecimal(1.0), wrappedFloatValue.getDecimal());
   }
}
