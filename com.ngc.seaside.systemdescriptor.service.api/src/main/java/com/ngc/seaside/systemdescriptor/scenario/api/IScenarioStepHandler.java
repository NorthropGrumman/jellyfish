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
