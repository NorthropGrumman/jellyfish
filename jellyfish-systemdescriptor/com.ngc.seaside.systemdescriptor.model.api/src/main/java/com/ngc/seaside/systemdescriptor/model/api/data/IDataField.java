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
