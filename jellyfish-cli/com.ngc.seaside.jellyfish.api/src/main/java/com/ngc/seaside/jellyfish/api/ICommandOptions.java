package com.ngc.seaside.jellyfish.api;

/**
 * The options necessary in order to run a command. All may not require any parameters but it is assumed that most
 * likely will.
 */
public interface ICommandOptions {

   /**
    * The parameters that the user entered when calling the command. This should return an empty list in the
    * event that the command doesn't require any parameters.
    *
    * @return the parameters.
    */
   IParameterCollection getParameters();

}
