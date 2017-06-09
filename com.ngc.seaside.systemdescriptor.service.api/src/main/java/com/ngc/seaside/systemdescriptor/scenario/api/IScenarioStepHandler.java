package com.ngc.seaside.systemdescriptor.scenario.api;

import java.util.Map;

public interface IScenarioStepHandler {

   Map<VerbTense, ScenarioStepVerb> getVerbs();
}
