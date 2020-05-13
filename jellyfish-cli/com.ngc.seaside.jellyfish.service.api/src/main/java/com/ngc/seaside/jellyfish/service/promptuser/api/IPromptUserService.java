/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
