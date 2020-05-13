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
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyEnumerationValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyPrimitiveValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyValue;

public abstract class PropertyValue implements IPropertyValue {

   private final DataTypes type;
   private final boolean isSet;

   /**
    * Constructs a PropertyValue
    *
    * @param type  type of property
    * @param isSet whether or not the property has been set
    * @throws IllegalArgumentException if the class does not implement the appropriate interface corresponding to the
    * property's type
    */
   public PropertyValue(DataTypes type, boolean isSet) {
      Preconditions.checkNotNull(type, "type may not be null!");
      if (type == DataTypes.DATA && !(this instanceof IPropertyDataValue)) {
         throw new IllegalArgumentException("type is DataTypes.DATA, but class does not implement IPropertyDataValue");
      }
      if (type == DataTypes.ENUM && !(this instanceof IPropertyEnumerationValue)) {
         throw new IllegalArgumentException(
               "type is DataTypes.ENUM, but class does not implement IPropertyEnumerationValue");
      }
      if ((type != DataTypes.DATA && type != DataTypes.ENUM) && !(this instanceof IPropertyPrimitiveValue)) {
         throw new IllegalArgumentException(
               "type is a primitive, but class does not implement IPropertyPrimitiveValue");
      }
      this.type = type;
      this.isSet = isSet;
   }

   @Override
   public DataTypes getType() {
      return type;
   }

   @Override
   public boolean isSet() {
      return isSet;
   }

   /**
    * @throws IllegalStateException if the value is not set
    */
   protected void checkIsSet() {
      if (!isSet()) {
         throw new IllegalStateException("Cannot get a value: the property value is not set");
      }
   }

}
