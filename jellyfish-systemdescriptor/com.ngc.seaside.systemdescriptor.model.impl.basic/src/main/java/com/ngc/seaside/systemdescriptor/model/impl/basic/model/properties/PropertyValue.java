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
