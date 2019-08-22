/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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
