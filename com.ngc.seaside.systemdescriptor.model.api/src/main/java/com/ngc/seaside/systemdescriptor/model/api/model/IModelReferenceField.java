package com.ngc.seaside.systemdescriptor.model.api.model;

/**
 * Represents a field declared in an {@link IModel} that references another model.  Operations that change the state of
 * this object may throw {@code UnsupportedOperationException}s if the object is immutable.
 */
public interface IModelReferenceField extends IReferenceField {

   /**
    * Gets the model type of this field that is being referenced.
    *
    * @return the model type of this field that is being referenced
    */
   IModel getType();

   /**
    * Sets the model type of this field that is being referenced
    *
    * @param model the model type of this field that is being referenced
    * @return this field
    */
   IModelReferenceField setType(IModel model);
}
