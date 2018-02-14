package com.ngc.seaside.systemdescriptor.validation.api;

/**
 * The various serenity levels associated with issues.
 */
public enum Severity {
   /**
    * The highest serenity level; indicates an fatal error and the descriptor is invalid.
    */
   ERROR,
   /**
    * Indicates a potential problem but the descriptor is valid.
    */
   WARNING,
   /**
    * Indicates a suggested fix to a valid descriptor.
    */
   SUGGESTION
}
