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
package com.ngc.seaside.systemdescriptor.extension;

import com.ngc.seaside.systemdescriptor.systemDescriptor.Step;

import java.util.Set;

/**
 * A plugin interface to add custom scenario step code completion at runtime.
 */
public interface IScenarioStepCompletionExtension {

   /**
    * Returns the list of possible keywords that can be used for the step, or an empty list if there are no possible
    * keywords.
    * 
    * @param step step currently being implemented
    * @param partialKeyword a non-null, potentially empty, keyword prefix
    * @return a list of possible keywords that can be used for the step
    */
   Set<String> completeKeyword(Step step, String partialKeyword);

   /**
    * Returns the list of possible values that can be used to complete the parameter at the given index, or an empty
    * list if there are no possible keywords.
    * 
    * @param step step currently being implemented
    * @param parameterIndex 0-based parameter of the step
    * @param insert if {@code true}, a new parameter is being inserted at the given index; otherwise, the parameter
    *           already at the given index should be completed
    * @return the list of possible parameter completion values
    */
   Set<String> completeStepParameter(Step step, int parameterIndex, boolean insert);
}
