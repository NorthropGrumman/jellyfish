package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.scenario;

import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Step;

import java.util.List;

public class WrappedScenarioStep<T extends Step> extends AbstractWrappedXtext<T> implements IScenarioStep {

  public WrappedScenarioStep(IWrapperResolver resolver, T wrapped) {
    super(resolver, wrapped);
  }

  @Override
  public String getKeyword() {
    return wrapped.getKeyword();
  }

  @Override
  public IScenarioStep setKeyword(String keyword) {
    wrapped.setKeyword(keyword);
    return this;
  }

  @Override
  public List<String> getParameters() {
    return wrapped.getParameters();
  }
}
