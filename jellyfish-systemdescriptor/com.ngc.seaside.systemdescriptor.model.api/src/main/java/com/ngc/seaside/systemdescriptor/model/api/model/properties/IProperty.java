package com.ngc.seaside.systemdescriptor.model.api.model.properties;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;

import java.util.Collection;
import java.util.Optional;

/**
 * A property is a combination of a <i>declaration</i> and an optional value. Properties can be declared on a variety of
 * elements, including models, fields, and links. Properties be declared to be of any of the defined {@link DataTypes
 * primitive data types}. Properties may also use a custom user defined data type or enumeration. Values of properties
 * can be investigated to determine the type of the property.
 */
public interface IProperty extends INamedChild<IProperties> {

   /**
    * Gets the type of this property. If the type is {@link DataTypes#DATA}, {@link #getData()} or {@link #getDatas()}
    * can be used obtain the values of the property. If the type is {@link DataTypes#ENUM}, {@link #getEnumerations()}
    * can be used obtain the values of the property. Finally, {@link #getPrimitives()} can be used for all other cases.
    *
    * @return the type of this property
    */
   DataTypes getType();
   
   /**
    * Gets the data type this property is referencing.
    *
    * @return the data type this property is referencing
    * @throws IllegalStateException if the {@link #getType() type} is not {@link DataTypes#DATA}
    */
   IData getReferencedDataType();
   
   /**
    * Gets the enumeration type this property is referencing.
    *
    * @return the enumeration type this property is referencing
    * @throws IllegalStateException if the {@link #getType() type} is not {@link DataTypes#ENUM}
    */
   IEnumeration getReferencedEnumeration();
   
   /**
    * Gets the cardinality of this property. This indicates if the property has at most a single value or many values.
    *
    * @return the cardinality of this property
    */
   FieldCardinality getCardinality();

   /**
    * Gets the value of this property.
    *
    * @return the value of this property
    * @throws IllegalStateException if the cardinality is not {@link FieldCardinality#SINGLE}
    */
   default IPropertyValue getValue() {
      switch (getType()) {
         case DATA:
            return getData();
         case ENUM:
            return getEnumeration();
         default:
            return getPrimitive();
      }
   }

   /**
    * Gets the data value of this property.
    *
    * @return the data value of this property
    * @throws IllegalStateException if the type of this property is not {@link DataTypes#DATA} or the cardinality is not
    *                               {@link FieldCardinality#SINGLE}
    */
   IPropertyDataValue getData();

   /**
    * Gets the enumeration value of this property.
    *
    * @return the enumeration value of this property
    * @throws IllegalStateException if the type of this property is not {@link DataTypes#ENUM} or the cardinality is not
    *                               {@link FieldCardinality#SINGLE}
    */
   IPropertyEnumerationValue getEnumeration();

   /**
    * Gets the primitive value of this property.
    *
    * @return the primitive value of this property
    * @throws IllegalStateException if the type of this property is not a primitive or the cardinality is not {@link
    *                               FieldCardinality#SINGLE}
    */
   IPropertyPrimitiveValue getPrimitive();

   /**
    * Gets the values of this property, or {@link Optional#empty()} if the property is not set.
    *
    * @return the value of this property
    * @throws IllegalStateException if the cardinality is not {@link FieldCardinality#MANY}
    */
   @SuppressWarnings("unchecked")
   default Optional<Collection<IPropertyValue>> getValues() {
      switch (getType()) {
         case DATA:
            return (Optional<Collection<IPropertyValue>>) (Optional<?>) getDatas();
         case ENUM:
            return (Optional<Collection<IPropertyValue>>) (Optional<?>) getEnumerations();
         default:
            return (Optional<Collection<IPropertyValue>>) (Optional<?>) getPrimitives();
      }
   }

   /**
    * Gets the data values of this property, or {@link Optional#empty()} if the property is not set.
    *
    * @return the data value of this property
    * @throws IllegalStateException if the type of this property is not {@link DataTypes#DATA} or the cardinality is not
    *                               {@link FieldCardinality#MANY}
    */
   Optional<Collection<IPropertyDataValue>> getDatas();

   /**
    * Gets the enumeration values of this property, or {@link Optional#empty()} if the property is not set.
    *
    * @return the enumeration value of this property
    * @throws IllegalStateException if the type of this property is not {@link DataTypes#ENUM} or the cardinality is not
    *                               {@link FieldCardinality#MANY}
    */
   Optional<Collection<IPropertyEnumerationValue>> getEnumerations();

   /**
    * Gets the data values of this property, or {@link Optional#empty()} if the property is not set.
    *
    * @return the data value of this property
    * @throws IllegalStateException if the type of this property is not a primitive type or the cardinality is not
    *                               {@link FieldCardinality#MANY}
    */
   Optional<Collection<IPropertyPrimitiveValue>> getPrimitives();

}
