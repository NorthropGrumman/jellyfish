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
package com.ngc.seaside.systemdescriptor.model.api.data;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;

/**
 * A field that is declared in an {@link IData} object.  A field has a {@link #getType() type}.  If the type is {@link
 * DataTypes#DATA}, the field's type references an {@link IData} object.  If the type is {@link DataTypes#ENUM}, the
 * field's type references an {@link IEnumeration} object. Otherwise, the type of the field is a primitive type.
 * Operations that change the state of this object may throw {@code UnsupportedOperationException}s if the object is
 * immutable.
 */
public interface IDataField extends INamedChild<IData> {

   /**
    * Gets the metadata associated with this field.
    *
    * @return the metadata associated with this field
    */
   IMetadata getMetadata();

   /**
    * Sets the metadata associated with this field.
    *
    * @param metadata the metadata associated with this field
    * @return this field
    */
   IDataField setMetadata(IMetadata metadata);

   /**
    * Gets the cardinality of the field.
    *
    * @return the cardinality of the field
    */
   FieldCardinality getCardinality();

   /**
    * Sets the cardinality of the field.
    *
    * @param cardinality the cardinality of the field
    * @return this field
    */
   IDataField setCardinality(FieldCardinality cardinality);

   /**
    * Gets the type of this field.
    */
   DataTypes getType();

   /**
    * Sets the type of this field.
    *
    * @param type the type of this field
    * @return this field
    */
   IDataField setType(DataTypes type);

   /**
    * Gets the data type this field is referencing.
    *
    * @return the data type this field is referencing
    */
   IData getReferencedDataType();

   /**
    * Sets the data type this field is referencing.  This value is only set if {@link #getType()} is {@link
    * DataTypes#DATA}.
    *
    * @param dataType the data type this field is referencing
    * @return the data type this field is referencing
    */
   IDataField setReferencedDataType(IData dataType);

   /**
    * Gets the enumeration type this field is referencing.  This value is only set if {@link #getType()} is {@link
    * DataTypes#ENUM}.
    *
    * @return the enumeration type this field is referencing
    */
   IEnumeration getReferencedEnumeration();

   /**
    * Sets the enumeration type this field is referencing.
    *
    * @param enumeration the enumeration type this field is referencing
    * @return the enumeration type this field is referencing
    */
   IDataField setReferencedEnumeration(IEnumeration enumeration);
}
