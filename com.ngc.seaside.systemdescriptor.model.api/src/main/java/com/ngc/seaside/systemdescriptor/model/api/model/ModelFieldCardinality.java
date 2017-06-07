package com.ngc.seaside.systemdescriptor.model.api.model;

/**
 * Represents the cardinality of field declared within an {@link IModel}.
 */
public enum ModelFieldCardinality {
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
