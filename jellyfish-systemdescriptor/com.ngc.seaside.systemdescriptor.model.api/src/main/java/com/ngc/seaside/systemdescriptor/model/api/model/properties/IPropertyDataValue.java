package com.ngc.seaside.systemdescriptor.model.api.model.properties;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;

import java.util.Collection;
import java.util.Optional;

/**
 * Contains the value of a property when that value is a user defined data type.  This type of property value may be
 * defined recursively.  Note that if {@link #isSet()} returns true, all fields (including recursively defined) fields
 * will have values.
 */
public interface IPropertyDataValue extends IPropertyValue {

   /**
    * Gets the data type that the property is declared as.
    *
    * @return the data type that the property is declared as
    */
   IData getReferencedDataType();

   /**
    * A convenience operation to get a field of this property value by name.
    *
    * @param name the name of the field
    * @return an optional that contains the field if the field was found or an empty optional if the field was not found
    */
   default Optional<IDataField> getFieldByName(String name) {
      return getReferencedDataType().getFields().getByName(name);
   }

   /**
    * Gets the value of the given field.
    *
    * @param field the field to get the value for
    * @return the value of the given data field
    * @throws IllegalArgumentException if the field is not a member of the {@link #getReferencedDataType() referenced data type}
    * @throws IllegalStateException if the field's cardinality is not {@link FieldCardinality#SINGLE}
    */
   IPropertyValue getValue(IDataField field);

   /**
    * Gets the primitive value of the given field.
    *
    * @param field the field to get the value for
    * @return the primitive value of the given data field
    * @throws IllegalArgumentException if the field is not a member of the {@link #getReferencedDataType() referenced data type}
    * @throws IllegalStateException if the field's type is not primitive or the field's cardinality is not {@link FieldCardinality#SINGLE}
    */
   IPropertyPrimitiveValue getPrimitive(IDataField field);

   /**
    * Gets the enumeration value of the given field.
    *
    * @param field the field to get the value for
    * @return the enumeration value of the given data field
    * @throws IllegalArgumentException if the field is not a member of the {@link #getReferencedDataType() referenced data type}
    * @throws IllegalStateException if the field's type is not {@link DataTypes#ENUM} or the field's cardinality is not {@link FieldCardinality#SINGLE}
    */
   IPropertyEnumerationValue getEnumeration(IDataField field);

   /**
    * Gets the data value of the given field.
    *
    * @param field the field to get the value for
    * @return the enumeration value of the given data field
    * @throws IllegalArgumentException if the field is not a member of the {@link #getReferencedDataType() referenced data type}
    * @throws IllegalStateException if the field's type is not {@link DataTypes#DATA} or the field's cardinality is not {@link FieldCardinality#SINGLE}
    */
   IPropertyDataValue getData(IDataField field);

   /**
    * Gets the values of the given field.
    *
    * @param field the field to get the value for
    * @return the values of the given data field
    * @throws IllegalArgumentException if the field is not a member of the {@link #getReferencedDataType() referenced data type}
    * @throws IllegalStateException if the field's cardinality is not {@link FieldCardinality#MANY}
    */
   Collection<IPropertyValue> getValues(IDataField field);

   /**
    * Gets the primitive values of the given field.
    *
    * @param field the field to get the value for
    * @return the primitives value of the given data field
    * @throws IllegalArgumentException if the field is not a member of the {@link #getReferencedDataType() referenced data type}
    * @throws IllegalStateException if the field's type is not primitive or the field's cardinality is not {@link FieldCardinality#MANY}
    */
   Collection<IPropertyPrimitiveValue> getPrimitives(IDataField field);

   /**
    * Gets the enumeration values of the given field.
    *
    * @param field the field to get the value for
    * @return the enumeration values of the given data field
    * @throws IllegalArgumentException if the field is not a member of the {@link #getReferencedDataType() referenced data type}
    * @throws IllegalStateException if the field's type is not {@link DataTypes#ENUM} or the field's cardinality is not {@link FieldCardinality#MANY}
    */
   Collection<IPropertyEnumerationValue> getEnumerations(IDataField field);

   /**
    * Gets the data values of the given field.
    *
    * @param field the field to get the value for
    * @return the enumeration values of the given data field
    * @throws IllegalArgumentException if the field is not a member of the {@link #getReferencedDataType() referenced data type}
    * @throws IllegalStateException if the field's type is not {@link DataTypes#DATA} or the field's cardinality is not {@link FieldCardinality#MANY}
    */
   Collection<IPropertyDataValue> getDatas(IDataField field);

}
