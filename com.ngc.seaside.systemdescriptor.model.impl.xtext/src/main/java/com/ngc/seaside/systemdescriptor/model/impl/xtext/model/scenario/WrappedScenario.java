package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.scenario;

import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.collection.AutoWrappingCollection;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Scenario;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

public class WrappedScenario extends AbstractWrappedXtext<Scenario> implements IScenario {

  private Collection<IScenarioStep> givens;
  private Collection<IScenarioStep> whens;
  private Collection<IScenarioStep> thens;

  public WrappedScenario(IWrapperResolver resolver, Scenario wrapped) {
    super(resolver, wrapped);

    if (wrapped.getGiven() == null) {
      givens = new ScenarioInitializingCollection<>(
          s -> new WrappedScenarioStep<>(resolver, s),
          WrappedScenarioStep::toXtextGivenStep,
          () -> {
            wrapped.setGiven(SystemDescriptorFactory.eINSTANCE.createGivenDeclaration());
            return wrapped.getGiven().getSteps();
          });
    } else {
      givens = new AutoWrappingCollection<>(
          wrapped.getGiven().getSteps(),
          s -> new WrappedScenarioStep<>(resolver, s),
          WrappedScenarioStep::toXtextGivenStep);
    }

    if (wrapped.getWhen() == null) {
      whens = new ScenarioInitializingCollection<>(
          s -> new WrappedScenarioStep<>(resolver, s),
          WrappedScenarioStep::toXtextWhenStep,
          () -> {
            wrapped.setWhen(SystemDescriptorFactory.eINSTANCE.createWhenDeclaration());
            return wrapped.getWhen().getSteps();
          });
    } else {
      whens = new AutoWrappingCollection<>(
          wrapped.getWhen().getSteps(),
          s -> new WrappedScenarioStep<>(resolver, s),
          WrappedScenarioStep::toXtextWhenStep);
    }

    if (wrapped.getThen() == null) {
      thens = new ScenarioInitializingCollection<>(
          s -> new WrappedScenarioStep<>(resolver, s),
          WrappedScenarioStep::toXtextThenStep,
          () -> {
            wrapped.setThen(SystemDescriptorFactory.eINSTANCE.createThenDeclaration());
            return wrapped.getThen().getSteps();
          });
    } else {
      thens = new AutoWrappingCollection<>(
          wrapped.getThen().getSteps(),
          s -> new WrappedScenarioStep<>(resolver, s),
          WrappedScenarioStep::toXtextThenStep);
    }
  }

  @Override
  public Collection<IScenarioStep> getGivens() {
    return givens;
  }

  @Override
  public Collection<IScenarioStep> getWhens() {
    return whens;
  }

  @Override
  public Collection<IScenarioStep> getThens() {
    return thens;
  }

  @Override
  public String getName() {
    return wrapped.getName();
  }

  @Override
  public IModel getParent() {
    return resolver.getWrapperFor((Model) wrapped.eContainer());
  }

  private static class ScenarioInitializingCollection<X extends EObject, T> extends AutoWrappingCollection<X, T> {

    private final Supplier<EList<X>> initializer;
    private boolean hasSceanrioBeenInitialized = false;

    private ScenarioInitializingCollection(Function<X, T> wrapperFunction,
                                           Function<T, X> unwrapperFunction,
                                           Supplier<EList<X>> initializer) {
      super(new BasicEList<>(), wrapperFunction, unwrapperFunction);
      this.initializer = initializer;
    }

    @Override
    public boolean add(T t) {
      if (!hasSceanrioBeenInitialized) {
        hasSceanrioBeenInitialized = true;
        setWrapped(initializer.get());
      }
      return super.add(t);
    }
  }
}
