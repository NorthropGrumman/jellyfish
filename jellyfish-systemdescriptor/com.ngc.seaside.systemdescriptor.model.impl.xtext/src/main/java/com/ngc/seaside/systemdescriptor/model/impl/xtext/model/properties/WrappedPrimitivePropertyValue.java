package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.properties;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyPrimitiveValue;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.exception.UnrecognizedXtextTypeException;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.BooleanValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.IntValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyValue;
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
      Preconditions.checkState(isSet(), "property is not set!");
      Preconditions.checkState(getType() == DataTypes.INT, "property is not an integer!");
      return new BigInteger(Integer.toString(((IntValue) wrapped).getValue()));
   }

   @Override
   public BigDecimal getDecimal() {
      return null;
   }

   @Override
   public boolean getBoolean() {
      Preconditions.checkState(isSet(), "property is not set!");
      Preconditions.checkState(getType() == DataTypes.BOOLEAN, "property is not a boolean!");
      return Boolean.parseBoolean(((BooleanValue) wrapped).getValue());
   }

   @Override
   public String getString() {
      return null;

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
      return (getType() == DataTypes.BOOLEAN && ((BooleanValue) wrapped).getValue() != null) ||
             getType() == DataTypes.INT;
   }
}
