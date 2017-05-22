package com.ngc.seaside.systemdescriptor.model.api.model.scenario;

import java.util.List;

public interface IScenarioStep {

  String getKeyword();

  IScenarioStep setKeyword(String keyword);

  List<String> getParameters();
}
