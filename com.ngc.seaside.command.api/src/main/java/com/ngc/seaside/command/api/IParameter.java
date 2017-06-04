package com.ngc.seaside.command.api;

/**
 * @author justan.provence@ngc.com
 */
public interface IParameter {
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
   String getValue();

   /**
    * Determine if the parameter is required.
    *
    * @return true if the parameter is required.
    */
   boolean isRequired();
}
