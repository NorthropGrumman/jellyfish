package com.ngc.seaside.command.api;

import java.util.List;

/**
 * The usage of a Command and the Command provider. The command provider implementations may only require the
 * description.
 */
public interface IUsage {
   /**
    * Get a description as to what the command does.
    *
    * @return the description
    */
   String getDescription();

   /**
    * Get the list of parameters.
    * They should be ordered such that all optional parameters are at the end.
    *
    * @return the list of parameters.
    */
   List<IParameter<?>> getAllParameters();

   /**
    * Get the list of required parameters. The value names should not be in brackets.
    *
    * @return the list of required parameters.
    */
   List<IParameter<?>> getRequiredParameters();
}
