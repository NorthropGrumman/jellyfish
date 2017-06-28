package com.ngc.seaside.systemdescriptor.model.api.data;

import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;

/**
 * A field that is declared in an {@link IData} object.  Operations that change the state of this object may throw
 * {@code UnsupportedOperationException}s if the object is immutable.
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
    * Gets the data type this field is referencing
    *
    * @return the data type this field is referencing
    */
   IData getReferencedDataType();

   /**
    * Sets the data type this field is referencing.
    *
    * @param dataType the data tyep this field is referencing
    * @return the data type this field is referencing
    */
   IDataField setReferencedDataType(IData dataType);
}
