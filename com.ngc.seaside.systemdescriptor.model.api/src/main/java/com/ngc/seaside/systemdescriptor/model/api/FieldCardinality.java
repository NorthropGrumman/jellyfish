package com.ngc.seaside.systemdescriptor.model.api;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

/**
 * Represents the cardinality of field declared within an {@link IModel} or {@link IData}.
 */
public enum FieldCardinality {
   /**
    * Indicates that a single field really represents multiple ports and may receive, transmit, or be associated with
    * more than one instance of the type declared by the field.
    */
   MANY,
   /**
    * The default value which indicates a single field is associated with exactly one instance of the type declared by
    * the field.
    */
   SINGLE
}
