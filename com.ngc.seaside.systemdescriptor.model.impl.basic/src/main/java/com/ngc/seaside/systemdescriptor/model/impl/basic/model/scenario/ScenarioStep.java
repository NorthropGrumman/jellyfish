package com.ngc.seaside.systemdescriptor.model.impl.basic.model.scenario;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ScenarioStep implements IScenarioStep {

  protected final List<String> parameters;
  protected String keyword;

  private ScenarioStep(List<String> parameters) {
    this.parameters = parameters;
  }

  public ScenarioStep() {
    this.parameters = new ArrayList<>();
  }

  @Override
  public String getKeyword() {
    return keyword;
  }

  @Override
  public IScenarioStep setKeyword(String keyword) {
    this.keyword = keyword;
    return this;
  }

  @Override
  public List<String> getParameters() {
    return parameters;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ScenarioStep)) {
      return false;
    }
    ScenarioStep that = (ScenarioStep) o;
    return Objects.equals(parameters, that.parameters) &&
           Objects.equals(keyword, that.keyword);
  }

  @Override
  public int hashCode() {
    return Objects.hash(parameters, keyword);
  }

  @Override
  public String toString() {
    return "ScenarioStep[" +
           "parameters=" + parameters +
           ", keyword='" + keyword + '\'' +
           ']';
  }

  public static IScenarioStep immutable(IScenarioStep step) {
    Preconditions.checkNotNull(step, "step may not be null!");

    ImmutableScenarioStep immutable = new ImmutableScenarioStep(
        Collections.unmodifiableList(
            new ArrayList<>(step.getParameters())));
    immutable.keyword = step.getKeyword();
    return immutable;
  }

  private static class ImmutableScenarioStep extends ScenarioStep {

    private ImmutableScenarioStep(List<String> parameters) {
      super(parameters);
    }

    @Override
    public IScenarioStep setKeyword(String keyword) {
      throw new UnsupportedOperationException("object is not modifiable!");
    }
  }
}
