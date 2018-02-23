package com.ngc.seaside.systemdescriptor.model.api.model;

import java.util.Optional;

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
   
   /**
    * Gets the field that this field refines. If this field does not refine a field, the optional is empty.
    * 
    * @return the field that this field refines
    */
   Optional<IModelReferenceField> getRefinedField();

}
