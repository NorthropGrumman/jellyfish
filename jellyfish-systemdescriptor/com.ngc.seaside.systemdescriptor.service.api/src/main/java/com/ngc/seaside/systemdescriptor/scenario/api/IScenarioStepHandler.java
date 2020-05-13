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
package com.ngc.seaside.systemdescriptor.scenario.api;

import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * A step handler is register to handle a particular verb that is used as a {@link IScenarioStep#getKeyword() keyword in
 * a scenario}.  A handle handles at most one verb, but it must handle all tenses (past, present, and future) of that
 * verb.
 */
public interface IScenarioStepHandler {

   /**
    * Gets a map keyed on {@code VerbTense} that contains the different tenses of the verb this handler supports.  The
    * map should contain at most three entities; one entry for each tense of the verb this handler supports.
    *
    * @return a unmodifiable map of all verbs this handler supports.
    */
   Map<VerbTense, ScenarioStepVerb> getVerbs();

   /**
    * Returns a collection of potential strings that can be used to complete the parameter at the given index, or an
    * empty list if there is none. The scenario step's parameters represent the currently filled in parameters for the
    * scenario step, possible containing empty string elements.
    *
    * @param step           scenario step
    * @param verb           scenario step verb
    * @param parameterIndex index of parameter
    * @return a collection of potential strings that can be used to complete the parameter at the given index
    */
   default Set<String> getSuggestedParameterCompletions(IScenarioStep step, ScenarioStepVerb verb,
            int parameterIndex) {
      return Collections.emptySet();
   }
}
