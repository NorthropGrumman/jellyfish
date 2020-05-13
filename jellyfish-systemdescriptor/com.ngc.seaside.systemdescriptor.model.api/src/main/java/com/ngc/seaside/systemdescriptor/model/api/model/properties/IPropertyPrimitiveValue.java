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
    * @throws IllegalStateException if this property is not {@link #isSet() set} or the type is not {@link
    *                               DataTypes#INT}
    */
   BigInteger getInteger();

   /**
    * Gets the decimal value of the property.
    *
    * @return the value of this property as a single decimal value
    * @throws IllegalStateException if this property is not {@link #isSet() set} or the type is not {@link
    *                               DataTypes#FLOAT}
    */
   BigDecimal getDecimal();

   /**
    * Gets the boolean value of the property.
    *
    * @return the value of this property as a single boolean value
    * @throws IllegalStateException if this property is not {@link #isSet() set} or the type is not {@link
    *                               DataTypes#BOOLEAN}
    */
   boolean getBoolean();

   /**
    * Gets the value of this property as a single string.
    *
    * @return the string value of the property
    * @throws IllegalStateException if this property is not {@link #isSet() set} or the type is not {@link
    *                               DataTypes#STRING}
    */
   String getString();

}
