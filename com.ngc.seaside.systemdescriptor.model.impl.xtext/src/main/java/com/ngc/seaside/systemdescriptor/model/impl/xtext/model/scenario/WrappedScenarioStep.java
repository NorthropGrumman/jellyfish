package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.scenario;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.GivenStep;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Step;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ThenStep;
import com.ngc.seaside.systemdescriptor.systemDescriptor.WhenStep;

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

  public static GivenStep toXtextGivenStep(IScenarioStep step) {
    Preconditions.checkNotNull(step, "step may not be null!");
    GivenStep x = SystemDescriptorFactory.eINSTANCE.createGivenStep();
    x.setKeyword(step.getKeyword());
    x.getParameters().addAll(step.getParameters());
    return x;
  }

  public static WhenStep toXtextWhenStep(IScenarioStep step) {
    Preconditions.checkNotNull(step, "step may not be null!");
    WhenStep x = SystemDescriptorFactory.eINSTANCE.createWhenStep();
    x.setKeyword(step.getKeyword());
    x.getParameters().addAll(step.getParameters());
    return x;
  }

  public static ThenStep toXtextThenStep(IScenarioStep step) {
    Preconditions.checkNotNull(step, "step may not be null!");
    ThenStep x = SystemDescriptorFactory.eINSTANCE.createThenStep();
    x.setKeyword(step.getKeyword());
    x.getParameters().addAll(step.getParameters());
    return x;
  }
}
