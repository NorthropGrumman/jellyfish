package com.ngc.seaside.systemdescriptor.model.api.model;

import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;

/**
 * A reference field is a field declared within an {@link IModel} that may reference either another {@code IModel} or
 * {@link com.ngc.seaside.systemdescriptor.model.api.data.IData} type.  Operations that change the state of this object
 * may throw {@code UnsupportedOperationException}s if the object is immutable.
 */
public interface IReferenceField extends INamedChild<IModel> {

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
   IReferenceField setMetadata(IMetadata metadata);

   /**
    * Gets the properties of this field.
    *
    * @return the properties of this field (never {@code null})
    */
   default IProperties getProperties() {
      // TODO: make this method abstract once implemented
      throw new UnsupportedOperationException("Not implemented");
   }

   /**
    * Sets the properties of this field.
    *
    * @param properties the properties of this field
    * @return this field
    */
   default IReferenceField setProperties(IProperties properties) {
      // TODO: make this method abstract once implemented
      throw new UnsupportedOperationException("Not implemented");
   }
}
