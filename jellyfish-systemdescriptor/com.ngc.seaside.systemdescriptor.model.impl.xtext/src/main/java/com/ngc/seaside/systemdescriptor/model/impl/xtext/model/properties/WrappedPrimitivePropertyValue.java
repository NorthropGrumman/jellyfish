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

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyPrimitiveValue;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.exception.UnrecognizedXtextTypeException;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.BooleanValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DblValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.IntValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.StringValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import java.math.BigDecimal;
import java.math.BigInteger;

public class WrappedPrimitivePropertyValue extends AbstractWrappedXtext<PropertyValue>
      implements IPropertyPrimitiveValue {

   public WrappedPrimitivePropertyValue(IWrapperResolver resolver, PropertyValue wrapped) {
      super(resolver, wrapped);
   }

   @Override
   public BigInteger getInteger() {
      Preconditions.checkState(getType() == DataTypes.INT, "property is not an integer!");
      return new BigInteger(Integer.toString(((IntValue) wrapped).getValue()));
   }

   @Override
   public BigDecimal getDecimal() {
      Preconditions.checkState(getType() == DataTypes.FLOAT, "property is not a floating-point number!");
      return new BigDecimal(((DblValue) wrapped).getValue());
   }

   @Override
   public boolean getBoolean() {
      Preconditions.checkState(getType() == DataTypes.BOOLEAN, "property is not a boolean!");
      return Boolean.parseBoolean(((BooleanValue) wrapped).getValue());
   }

   @Override
   public String getString() {
      Preconditions.checkState(getType() == DataTypes.STRING, "property is not a string!");
      return ((StringValue) wrapped).getValue();
   }

   @Override
   public DataTypes getType() {
      switch (wrapped.eClass().getClassifierID()) {
         case SystemDescriptorPackage.BOOLEAN_VALUE:
            return DataTypes.BOOLEAN;
         case SystemDescriptorPackage.INT_VALUE:
            return DataTypes.INT;
         case SystemDescriptorPackage.DBL_VALUE:
            return DataTypes.FLOAT;
         case SystemDescriptorPackage.STRING_VALUE:
            return DataTypes.STRING;
         default:
            throw new UnrecognizedXtextTypeException(wrapped);
      }
   }

   @Override
   public boolean isSet() {
      return true;
   }
}
