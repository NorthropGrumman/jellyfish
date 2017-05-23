package com.ngc.seaside.systemdescriptor.model.api.model;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;

/**
 * Represents a field declared in an {@link IModel} that references another {@link IData} type.  Operations that change
 * the state of this object may throw {@code UnsupportedOperationException}s if the object is immutable.
 */
public interface IDataReferenceField extends IReferenceField {

  /**
   * Gets the model type of this field that is being referenced
   *
   * @return the model type of this field that is being referenced
   */
  IData getType();

  /**
   * Sets the data type of this field that is being referenced.
   *
   * @param type the data type of this field that is being referenced
   * @return this field
   */
  IDataReferenceField setType(IData type);

  /**
   * Gets the cardinality associated with this field.
   *
   * @return the cardinality associated with this field
   */
  ModelFieldCardinality getCardinality();

  /**
   * Sets the cardinality associated with this field.
   *
   * @param cardinality the cardinality associated with this field
   * @return this field
   */
  IDataReferenceField setCardinality(ModelFieldCardinality cardinality);
}
