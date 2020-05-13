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
package com.ngc.seaside.systemdescriptor.model.impl.basic.model.properties;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyPrimitiveValue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

public class PropertyPrimitiveValue extends PropertyValue implements IPropertyPrimitiveValue {

   private final Object value;

   /**
    * Constructs an unset PropertyPrimitiveValue of the given type.
    *
    * @param type the value's type
    */
   public PropertyPrimitiveValue(DataTypes type) {
      super(type, false);
      this.value = null;
   }

   /**
    * Creates a new int value.
    */
   public PropertyPrimitiveValue(BigInteger value) {
      super(DataTypes.INT, true);
      Preconditions.checkNotNull(value, "value may not be null!");
      this.value = value;
   }

   /**
    * Creates a new decimal value.
    */
   public PropertyPrimitiveValue(BigDecimal value) {
      super(DataTypes.FLOAT, true);
      Preconditions.checkNotNull(value, "value may not be null!");
      this.value = value;
   }

   /**
    * Creates a new string value.
    */
   public PropertyPrimitiveValue(String value) {
      super(DataTypes.STRING, true);
      Preconditions.checkNotNull(value, "value may not be null!");
      this.value = value;
   }

   /**
    * Creates a new boolean value.
    */
   public PropertyPrimitiveValue(boolean value) {
      super(DataTypes.BOOLEAN, true);
      this.value = value;
   }

   @Override
   public BigInteger getInteger() {
      checkType(DataTypes.INT);
      return (BigInteger) value;
   }

   @Override
   public BigDecimal getDecimal() {
      checkType(DataTypes.FLOAT);
      return (BigDecimal) value;
   }

   @Override
   public boolean getBoolean() {
      checkType(DataTypes.BOOLEAN);
      return (Boolean) value;
   }

   @Override
   public String getString() {
      checkType(DataTypes.STRING);
      return (String) value;
   }

   private void checkType(DataTypes expectedType) {
      super.checkIsSet();
      if (getType() != expectedType) {
         throw new IllegalStateException(
               "Cannot get a value of type " + expectedType + ": actual type is " + getType());
      }
   }

   @Override
   public int hashCode() {
      return Objects.hash(value);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (!(obj instanceof PropertyPrimitiveValue)) {
         return false;
      }
      PropertyPrimitiveValue that = (PropertyPrimitiveValue) obj;
      return Objects.equals(value, that.value);
   }

   @Override
   public String toString() {
      return "PropertyPrimitiveValue[type=" + getType() + ", value=" + value + "]";
   }

}
