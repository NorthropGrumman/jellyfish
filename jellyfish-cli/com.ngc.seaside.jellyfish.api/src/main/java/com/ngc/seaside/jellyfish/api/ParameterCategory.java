package com.ngc.seaside.jellyfish.api;

/**
 * An enumeration describing the "usefulness" of a jellyfish command parameter to a typical user
 */
public enum ParameterCategory {
   /**
    * Represents a parameter that is required to run a command.
    */
   REQUIRED,
   
   /**
    * Represents a parameter that is not required, but the user might want to specify.
    */
   USEFUL,
   
   /**
    * Represents a parameter that only an advanced jellyfish user will ever want to use.
    */
   ADVANCED
}
