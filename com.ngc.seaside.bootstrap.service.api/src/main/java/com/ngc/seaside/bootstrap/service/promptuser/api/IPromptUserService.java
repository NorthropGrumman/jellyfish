package com.ngc.seaside.bootstrap.service.promptuser.api;

import java.util.function.Predicate;

/**
 * This interface provides method for prompting the user for input. The intended usage for this class would be to
 * allow for ICommand and ICommandProvider implementations to prompt the user for required input on the command
 * line.
 */
public interface IPromptUserService {
   /**
    * Queries the user to enter a value for the given parameter.
    *
    * @param parameter    name of the parameter
    * @param defaultValue default value for the parameter, or null
    * @param validator    function to determine whether a value is valid or not (can be null)
    * @return a valid value for the parameter
    */
   String prompt(String parameter, String defaultValue, Predicate<String> validator);
}
