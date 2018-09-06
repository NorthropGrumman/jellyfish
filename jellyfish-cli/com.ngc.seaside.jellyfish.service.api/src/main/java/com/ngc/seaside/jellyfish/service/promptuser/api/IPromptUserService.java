/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.jellyfish.service.promptuser.api;

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

   /**
    * Utilize a custom question to prompt the user to enter data.
    *
    * @param question      the question of the prompt. What are you asking?
    * @param note          the note to clarify the accepted input types along with providing the default.
    * @param defaultValue  the default value will be included in the note. This is the default value set if they just
    *                      hit enter.
    * @param validator     Optional.
    * @return a valid value for the prompt.
    */
   String promptDataEntry(String question, String note, String defaultValue, Predicate<String> validator);
}
