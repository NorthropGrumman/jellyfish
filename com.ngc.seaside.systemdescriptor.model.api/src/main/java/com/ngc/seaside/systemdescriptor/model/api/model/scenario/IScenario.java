package com.ngc.seaside.systemdescriptor.model.api.model.scenario;

import java.util.List;

public interface IScenario {

  String getName();

  List<IScenarioStep> getGivens();

  List<IScenarioStep> getWhens();

  List<IScenarioStep> getThens();

  String getParentModel();
}
