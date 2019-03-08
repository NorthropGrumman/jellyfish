/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
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
