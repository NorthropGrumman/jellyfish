package com.ngc.seaside.systemdescriptor.scenario.api;

import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;

/**
 * Described the different tenses of verbs that are referenced by {@link IScenarioStep#getKeyword() by keyword in
 * scenarios} and provided by {@link IScenarioStepHandler}s.
 */
public enum VerbTense {
   PAST_TENSE,
   PRESENT_TENSE,
   FUTURE_TENSE
}
