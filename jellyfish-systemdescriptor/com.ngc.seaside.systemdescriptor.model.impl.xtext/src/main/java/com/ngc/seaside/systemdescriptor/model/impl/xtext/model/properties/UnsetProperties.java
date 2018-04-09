package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.properties;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyEnumerationValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyPrimitiveValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyValues;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Contains constants for unset property values.
 */
public class UnsetProperties {

   private UnsetProperties() {
   }

   public static final IPropertyPrimitiveValue UNSET_PRIMITIVE_VALUE = new IPropertyPrimitiveValue() {
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

   public static final IPropertyEnumerationValue UNSET_ENUMERATION_VALUE = new IPropertyEnumerationValue() {
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

   public static final IPropertyDataValue UNSET_DATA_VALUE = new IPropertyDataValue() {
      @Override
      public IData getReferencedDataType() {
         throw new IllegalStateException("this value is not set!");
      }

      @Override
      public IPropertyPrimitiveValue getPrimitive(IDataField field) {
         throw new IllegalStateException("this value is not set!");
      }

      @Override
      public IPropertyEnumerationValue getEnumeration(IDataField field) {
         throw new IllegalStateException("this value is not set!");
      }

      @Override
      public IPropertyDataValue getData(IDataField field) {
         throw new IllegalStateException("this value is not set!");
      }

      @Override
      public IPropertyValues<IPropertyPrimitiveValue> getPrimitives(IDataField field) {
         throw new IllegalStateException("this value is not set!");
      }

      @Override
      public IPropertyValues<IPropertyEnumerationValue> getEnumerations(IDataField field) {
         throw new IllegalStateException("this value is not set!");
      }

      @Override
      public IPropertyValues<IPropertyDataValue> getDatas(IDataField field) {
         throw new IllegalStateException("this value is not set!");
      }

      @Override
      public DataTypes getType() {
         return DataTypes.DATA;
      }

      @Override
      public boolean isSet() {
         return false;
      }
   };
}
