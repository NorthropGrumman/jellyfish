package com.ngc.seaside.systemdescriptor.model.api.model.properties;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;

/**
 * Contains the value of a property.  The type of value depends on the type of the property.  Properties with primitive
 * types have {@link IPropertyPrimitiveValue}s.  Properties with enumeration types have {@link
 * IPropertyEnumerationValue}s.  Properties with data types have {@link IPropertyDataValue}s.
 */
public interface IPropertyValue {

   /**
    * Gets the type of the value.
    * 
    * @return the type of the value
    */
   DataTypes getType();

   /**
    * Returns true if the property has a value set.
    *
    * @return true if the property has a value set, false if the property does not have a value
    */
   boolean isSet();

   /**
    * Returns true if the value of this property is a primitive.  If true, this object can be safely cast to {@link
    * IPropertyPrimitiveValue}.
    *
    * @return true if the value of this property is a primitive, false otherwise.
    */
   default boolean isPrimitive() {
      return this instanceof IPropertyPrimitiveValue;
   }

   /**
    * Returns true if the value of this property is a data object.  If true, this object can be safely cast to {@link
    * IPropertyDataValue}.
    *
    * @return true if the value of this property is a data object, false otherwise.
    */
   default boolean isData() {
      return this instanceof IPropertyDataValue;
   }

   /**
    * Returns true if the value of this property is an enumeration.  If true, this object can be safely cast to {@link
    * IPropertyEnumerationValue}.
    *
    * @return true if the value of this property is an enumeration, false otherwise.
    */
   default boolean isEnumeration() {
      return this instanceof IPropertyEnumerationValue;
   }
}
