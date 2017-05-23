package com.ngc.seaside.systemdescriptor.model.impl.basic.model.scenario;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;

import java.util.List;
import java.util.Objects;

public class Scenario implements IScenario {

  protected final String name;

  public Scenario(String name) {
    Preconditions.checkNotNull(name, "name may not be null!");
    Preconditions.checkArgument(!name.trim().isEmpty(), "name may not be empty!");
    this.name = name;
  }

  @Override
  public String getName() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public IModel getParent() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public List<IScenarioStep> getGivens() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public List<IScenarioStep> getWhens() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public List<IScenarioStep> getThens() {
    throw new UnsupportedOperationException("not implemented");
  }



}
