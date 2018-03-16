package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.properties;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyEnumerationValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyPrimitiveValue;

import java.math.BigDecimal;
import java.math.BigInteger;

public class UnsetProperties {

   private UnsetProperties() {
   }

   public final static IPropertyPrimitiveValue UNSET_PRIMITIVE_VALUE = new IPropertyPrimitiveValue() {
      @Override
      public BigInteger getInteger() {
         throw new IllegalStateException("this value is not set!");
      }

      @Override
      public BigDecimal getDecimal() {
         throw new IllegalStateException("this value is not set!");
      }

      @Override
      public boolean getBoolean() {
         throw new IllegalStateException("this value is not set!");
      }

      @Override
      public String getString() {
         throw new IllegalStateException("this value is not set!");
      }

      @Override
      public DataTypes getType() {
         throw new IllegalStateException("this value is not set!");
      }

      @Override
      public boolean isSet() {
         return false;
      }
   };

   public final static IPropertyEnumerationValue UNSET_ENUMERATION_VALUE = new IPropertyEnumerationValue() {
      @Override
      public IEnumeration getReferencedEnumeration() {
         throw new IllegalStateException("this value is not set!");
      }

      @Override
      public String getValue() {
         throw new IllegalStateException("this value is not set!");
      }

      @Override
      public DataTypes getType() {
         return DataTypes.ENUM;
      }

      @Override
      public boolean isSet() {
         return false;
      }
   };
}
