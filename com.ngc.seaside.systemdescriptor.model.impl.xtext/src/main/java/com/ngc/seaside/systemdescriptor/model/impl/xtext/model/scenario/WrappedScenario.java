package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.scenario;

import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.GivenStep;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Scenario;

import java.util.List;

public class WrappedScenario extends AbstractWrappedXtext<Scenario> implements IScenario {

  //private final WrappedScenarioStep<GivenStep> givens;

  public WrappedScenario(IWrapperResolver resolver, Scenario wrapped) {
    super(resolver, wrapped);
    //hivens = new WrappedScenarioStep<GivenStep>(resolver, wrapped.getGiven().getSteps());
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

  @Override
  public String getName() {
    return wrapped.getName();
  }

  @Override
  public IModel getParent() {
    return resolver.getWrapperFor((Model) wrapped.eContainer());
  }
}
