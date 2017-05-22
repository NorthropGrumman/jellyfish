package com.ngc.seaside.systemdescriptor.model.api.model.scenario;

import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.List;

public interface IScenario extends INamedChild<IModel> {

  List<IScenarioStep> getGivens();

  List<IScenarioStep> getWhens();

  List<IScenarioStep> getThens();
}
