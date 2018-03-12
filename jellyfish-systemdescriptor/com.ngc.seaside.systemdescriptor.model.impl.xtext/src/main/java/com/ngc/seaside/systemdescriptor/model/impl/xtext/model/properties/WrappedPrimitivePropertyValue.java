package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.properties;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyPrimitiveValue;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyValue;

import java.math.BigDecimal;
import java.math.BigInteger;

public class WrappedPrimitivePropertyValue extends AbstractWrappedXtext<PropertyValue>
      implements IPropertyPrimitiveValue {
   public WrappedPrimitivePropertyValue(IWrapperResolver resolver, PropertyValue wrapped) {
      super(resolver, wrapped);
   }

   @Override
   public BigInteger getInteger() {
      return null;
   }

   @Override
   public BigDecimal getDecimal() {
      return null;
   }

   @Override
   public boolean getBoolean() {
      return false;
   }

   @Override
   public String getString() {
      return null;

   }

   @Override
   public DataTypes getType() {
      return null;
   }

   @Override
   public boolean isSet() {
      return false;
   }
}
