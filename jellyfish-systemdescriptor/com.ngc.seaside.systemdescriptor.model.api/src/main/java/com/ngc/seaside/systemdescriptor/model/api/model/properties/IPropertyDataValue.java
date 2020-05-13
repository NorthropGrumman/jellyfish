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

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;

import java.util.Optional;

/**
 * Contains the value of a property when that value is a user defined data type. This type of property value may be
 * defined recursively. Note that if {@link #isSet()} returns true, all fields (including recursively defined) fields
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
    * @throws IllegalArgumentException if the field is not a member of the {@link #getReferencedDataType() referenced
    *                                  data type}
    * @throws IllegalStateException    if the field's cardinality is not {@link FieldCardinality#SINGLE}
    */
   default IPropertyValue getValue(IDataField field) {
      Preconditions.checkNotNull(field, "field may not be null!");
      switch (field.getType()) {
         case DATA:
            return getData(field);
         case ENUM:
            return getEnumeration(field);
         default:
            return getPrimitive(field);
      }
   }

   /**
    * Gets the primitive value of the given field.
    *
    * @param field the field to get the value for
    * @return the primitive value of the given data field
    * @throws IllegalArgumentException if the field is not a member of the {@link #getReferencedDataType() referenced
    *                                  data type}
    * @throws IllegalStateException    if the field's type is not primitive or the field's cardinality is not {@link
    *                                  FieldCardinality#SINGLE}
    */
   IPropertyPrimitiveValue getPrimitive(IDataField field);

   /**
    * Gets the enumeration value of the given field.
    *
    * @param field the field to get the value for
    * @return the enumeration value of the given data field
    * @throws IllegalArgumentException if the field is not a member of the {@link #getReferencedDataType() referenced
    *                                  data type}
    * @throws IllegalStateException    if the field's type is not {@link DataTypes#ENUM} or the field's cardinality is
    *                                  not {@link FieldCardinality#SINGLE}
    */
   IPropertyEnumerationValue getEnumeration(IDataField field);

   /**
    * Gets the data value of the given field.
    *
    * @param field the field to get the value for
    * @return the data value of the given data field
    * @throws IllegalArgumentException if the field is not a member of the {@link #getReferencedDataType() referenced
    *                                  data type}
    * @throws IllegalStateException    if the field's type is not {@link DataTypes#DATA} or the field's cardinality is
    *                                  not {@link FieldCardinality#SINGLE}
    */
   IPropertyDataValue getData(IDataField field);

   /**
    * Gets the values of the given field.
    *
    * @param field the field to get the value for
    * @return the values of the given data field
    * @throws IllegalArgumentException if the field is not a member of the {@link #getReferencedDataType() referenced
    *                                  data type}
    * @throws IllegalStateException    if the field's cardinality is not {@link FieldCardinality#MANY}
    */
   default IPropertyValues<? extends IPropertyValue> getValues(IDataField field) {
      Preconditions.checkNotNull(field, "field may not be null!");
      switch (field.getType()) {
         case DATA:
            return getDatas(field);
         case ENUM:
            return getEnumerations(field);
         default:
            return getPrimitives(field);
      }
   }

   /**
    * Gets the primitive values of the given field.
    *
    * @param field the field to get the value for
    * @return the primitives value of the given data field
    * @throws IllegalArgumentException if the field is not a member of the {@link #getReferencedDataType() referenced
    *                                  data type}
    * @throws IllegalStateException    if the field's type is not primitive or the field's cardinality is not {@link
    *                                  FieldCardinality#MANY}
    */
   IPropertyValues<IPropertyPrimitiveValue> getPrimitives(IDataField field);

   /**
    * Gets the enumeration values of the given field.
    *
    * @param field the field to get the value for
    * @return the enumeration values of the given data field
    * @throws IllegalArgumentException if the field is not a member of the {@link #getReferencedDataType() referenced
    *                                  data type}
    * @throws IllegalStateException    if the field's type is not {@link DataTypes#ENUM} or the field's cardinality is
    *                                  not {@link FieldCardinality#MANY}
    */
   IPropertyValues<IPropertyEnumerationValue> getEnumerations(IDataField field);

   /**
    * Gets the data values of the given field.
    *
    * @param field the field to get the value for
    * @return the enumeration values of the given data field
    * @throws IllegalArgumentException if the field is not a member of the {@link #getReferencedDataType() referenced
    *                                  data type}
    * @throws IllegalStateException    if the field's type is not {@link DataTypes#DATA} or the field's cardinality is
    *                                  not {@link FieldCardinality#MANY}
    */
   IPropertyValues<IPropertyDataValue> getDatas(IDataField field);
}
