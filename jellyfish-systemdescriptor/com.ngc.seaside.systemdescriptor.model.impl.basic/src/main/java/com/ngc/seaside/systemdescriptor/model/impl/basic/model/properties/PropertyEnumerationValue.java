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
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyEnumerationValue;

import java.util.Objects;

public class PropertyEnumerationValue extends PropertyValue implements IPropertyEnumerationValue {

   private final IEnumeration enumeration;
   private final String value;

   /**
    * Creates a new enumeration value.
    */
   public PropertyEnumerationValue(IEnumeration enumeration) {
      super(DataTypes.ENUM, false);
      Preconditions.checkNotNull(enumeration, "enumeration may not be null!");
      this.enumeration = enumeration;
      this.value = null;
   }

   /**
    * Creates a new enumeration value.
    */
   public PropertyEnumerationValue(IEnumeration enumeration, String value) {
      super(DataTypes.ENUM, true);
      Preconditions.checkNotNull(enumeration, "enumeration may not be null!");
      Preconditions.checkNotNull(value, "value may not be null!");
      if (!enumeration.getValues().contains(value)) {
         throw new IllegalArgumentException(
               "value \"" + value + "\" is not an enumeration of " + enumeration.getFullyQualifiedName());
      }
      this.enumeration = enumeration;
      this.value = value;
   }


   @Override
   public IEnumeration getReferencedEnumeration() {
      return enumeration;
   }

   @Override
   public String getValue() {
      super.checkIsSet();
      return value;
   }

   @Override
   public int hashCode() {
      return Objects.hash(System.identityHashCode(enumeration), value);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (!(obj instanceof PropertyEnumerationValue)) {
         return false;
      }
      PropertyEnumerationValue that = (PropertyEnumerationValue) obj;
      return Objects.equals(value, that.value)
             && enumeration == that.enumeration;
   }

   @Override
   public String toString() {
      return "PropertyEnumerationValue[enumeration=" + enumeration.getFullyQualifiedName() + ", value=" + value + "]";
   }

}
