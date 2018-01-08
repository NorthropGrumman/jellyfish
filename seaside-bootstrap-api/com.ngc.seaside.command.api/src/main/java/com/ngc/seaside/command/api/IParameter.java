package com.ngc.seaside.command.api;

/**
 * An input parameter to a command.
 */
public interface IParameter<T> {

   /**
    * Get the description for this parameter.
    *
    * @return the parameter's description.
    */
   String getDescription();

   /**
    * Get the name of the parameter.
    *
    * @return the name.
    */
   String getName();

   /**
    * Get the value of the parameter.
    *
    * @return the value of the parameter.
    */
   T getValue();

   /**
    * Gets the string value of the parameter or an empty string if the value is {@code null}.
    *
    * @return the string value of the parameter or an empty string if the value is {@code null}
    */
   String getStringValue();

   /**
    * Determine if the parameter is required.
    *
    * @return true if the parameter is required.
    */
   boolean isRequired();
}
