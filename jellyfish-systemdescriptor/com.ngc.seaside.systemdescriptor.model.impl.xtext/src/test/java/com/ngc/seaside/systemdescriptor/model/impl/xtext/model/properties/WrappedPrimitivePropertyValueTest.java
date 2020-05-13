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
import com.ngc.seaside.systemdescriptor.systemDescriptor.BooleanValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DblValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.IntValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.StringValue;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WrappedPrimitivePropertyValueTest extends AbstractWrappedXtextTest {

   private WrappedPrimitivePropertyValue wrappedBooleanValue;
   private WrappedPrimitivePropertyValue wrappedIntegerValue;
   private WrappedPrimitivePropertyValue wrappedFloatValue;
   private WrappedPrimitivePropertyValue wrappedStringValue;

   private BooleanValue propertyBooleanValue = factory().createBooleanValue();
   private IntValue propertyIntegerValue = factory().createIntValue();
   private DblValue propertyFloatValue = factory().createDblValue();
   private StringValue propertyStringValue = factory().createStringValue();

   @Before
   public void setup() {
      wrappedBooleanValue = new WrappedPrimitivePropertyValue(resolver(), propertyBooleanValue);
      wrappedIntegerValue = new WrappedPrimitivePropertyValue(resolver(), propertyIntegerValue);
      wrappedFloatValue = new WrappedPrimitivePropertyValue(resolver(), propertyFloatValue);
      wrappedStringValue = new WrappedPrimitivePropertyValue(resolver(), propertyStringValue);
   }

   @Test
   public void testDoesReturnCorrectType() {
      assertEquals("boolean property type is incorrect!", wrappedBooleanValue.getType(), DataTypes.BOOLEAN);
      assertEquals("integer property type is incorrect!", wrappedIntegerValue.getType(), DataTypes.INT);
      assertEquals("float property type is incorrect!", wrappedFloatValue.getType(), DataTypes.FLOAT);
      assertEquals("string property type is incorrect!", wrappedStringValue.getType(), DataTypes.STRING);
   }

   @Test
   public void testDoesCorrectlyRetrieveSetPropertyValue() {
      propertyBooleanValue.setValue("true");
      wrappedBooleanValue = new WrappedPrimitivePropertyValue(resolver(), propertyBooleanValue);
      assertTrue("boolean property value is incorrect!", wrappedBooleanValue.getBoolean());
      assertTrue("value should be set!", wrappedBooleanValue.isSet());

      propertyBooleanValue.setValue("false");
      wrappedBooleanValue = new WrappedPrimitivePropertyValue(resolver(), propertyBooleanValue);
      assertFalse("boolean property value is incorrect!", wrappedBooleanValue.getBoolean());
      assertTrue("value should be set!", wrappedBooleanValue.isSet());

      propertyIntegerValue.setValue(-1);
      wrappedIntegerValue = new WrappedPrimitivePropertyValue(resolver(), propertyIntegerValue);
      assertEquals("integer property value is incorrect!", new BigInteger("-1"), wrappedIntegerValue.getInteger());
      assertTrue("value should be set!", wrappedIntegerValue.isSet());

      propertyIntegerValue.setValue(0);
      wrappedIntegerValue = new WrappedPrimitivePropertyValue(resolver(), propertyIntegerValue);
      assertEquals("integer property value is incorrect!", new BigInteger("0"), wrappedIntegerValue.getInteger());
      assertTrue("value should be set!", wrappedIntegerValue.isSet());

      propertyIntegerValue.setValue(1);
      wrappedIntegerValue = new WrappedPrimitivePropertyValue(resolver(), propertyIntegerValue);
      assertEquals("integer property value is incorrect!", new BigInteger("1"), wrappedIntegerValue.getInteger());
      assertTrue("value should be set!", wrappedIntegerValue.isSet());

      propertyFloatValue.setValue(-1.0);
      wrappedFloatValue = new WrappedPrimitivePropertyValue(resolver(), propertyFloatValue);
      assertEquals("float property value is incorrect!", new BigDecimal(-1.0), wrappedFloatValue.getDecimal());
      assertTrue("value should be set!", wrappedFloatValue.isSet());

      propertyFloatValue.setValue(0.0);
      wrappedFloatValue = new WrappedPrimitivePropertyValue(resolver(), propertyFloatValue);
      assertEquals("float property value is incorrect!", new BigDecimal(0.0), wrappedFloatValue.getDecimal());
      assertTrue("value should be set!", wrappedFloatValue.isSet());

      propertyFloatValue.setValue(1.0);
      wrappedFloatValue = new WrappedPrimitivePropertyValue(resolver(), propertyFloatValue);
      assertEquals("float property value is incorrect!", new BigDecimal(1.0), wrappedFloatValue.getDecimal());
      assertTrue("value should be set!", wrappedFloatValue.isSet());

      propertyStringValue.setValue("string");
      wrappedStringValue = new WrappedPrimitivePropertyValue(resolver(), propertyStringValue);
      assertEquals("string property value is incorrect!", "string", wrappedStringValue.getString());
      assertTrue("value should be set!", wrappedStringValue.isSet());
   }
}
