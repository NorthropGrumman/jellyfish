package com.ngc.seaside.systemdescriptor.service.help.api;

import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;

import java.util.Optional;

public interface IHelpService {

   Optional<String> getDescription(ScenarioStepVerb verb);
}
