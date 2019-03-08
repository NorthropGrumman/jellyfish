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

import java.util.Map;

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
}
