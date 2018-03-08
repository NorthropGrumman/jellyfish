package com.ngc.seaside.systemdescriptor.model.api.model.properties;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Contains the value of a property when that value is a primitive type.
 */
public interface IPropertyPrimitiveValue extends IPropertyValue {

   /**
    * Gets the integer value of the property.
    *
    * @return the value of this property as a single integer
    * @throws IllegalStateException if this property is not {@link #isSet() set} or the type is not {@link DataTypes#INT}
    */
   BigInteger getInteger();

   /**
    * Gets the decimal value of the property.
    *
    * @return the value of this property as a single decimal value
    * @throws IllegalStateException if this property is not {@link #isSet() set} or the type is not {@link DataTypes#FLOAT}
    */
   BigDecimal getDecimal();

   /**
    * Gets the boolean value of the property.
    *
    * @return the value of this property as a single boolean value
    * @throws IllegalStateException if this property is not {@link #isSet() set} or the type is not {@link DataTypes#BOOLEAN}
    */
   boolean getBoolean();

   /**
    * Gets the value of this property as a single string.
    *
    * @return the string value of the property
    * @throws IllegalStateException if this property is not {@link #isSet() set} or the type is not {@link DataTypes#STRING}
    */
   String getString();

}
