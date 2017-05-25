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

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Adapts a {@link Scenario} instance to {@link IScenario}.
 *
 * This class is not threadsafe.
 */
public class WrappedScenario extends AbstractWrappedXtext<Scenario> implements IScenario {

  // Thread safety note: Absolutely no part of this implementation is thread safe.

  private Collection<IScenarioStep> givens;
  private Collection<IScenarioStep> whens;
  private Collection<IScenarioStep> thens;

  public WrappedScenario(IWrapperResolver resolver, Scenario wrapped) {
    super(resolver, wrapped);

    // This next code looks terrible because we have to handle two cases when dealing with scenarios:
    // 1) The first case is easy.  The scenario has existing given, when, etc steps.  This means that
    // wrapped.getGiven(), wrapped.getWhen(), etc will not return null.  In this case, we just need to wrap the steps
    // as is, so we use the AutoWrappingCollection to wrap and unwrap on demand.
    // 2) The second case is harder.  If the scenario is empty (which is valid), wrapped.getGiven(), etc will return
    // null.  In this case we need to use the ScenarioInitializingCollection.  This collection will basically set a
    // new instance of a GivenDeclaration, WhenDeclaration, etc, when a new IScenarioStep is added to the collection
    // for the first time.  We can't just add empty declaration at construction time because that results in an
    // invalid model which XText won't accept.

    if (wrapped.getGiven() == null) {
      givens = new ScenarioInitializingCollection<>(
          s -> new WrappedScenarioStep<>(resolver, s),
          WrappedScenarioStep::toXtextGivenStep,
          () -> {
            // On the first add, create the GivenDeclaration and make the collection start wrapping the EList
            // within the declaration.
            wrapped.setGiven(SystemDescriptorFactory.eINSTANCE.createGivenDeclaration());
            return wrapped.getGiven().getSteps();
          });
    } else {
      // Otherwise, just wrap the steps that are in the existing declaration.
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
            // On the first add, create the WhenDeclaration and make the collection start wrapping the EList
            // within the declaration.
            wrapped.setWhen(SystemDescriptorFactory.eINSTANCE.createWhenDeclaration());
            return wrapped.getWhen().getSteps();
          });
    } else {
      // Otherwise, just wrap the steps that are in the existing declaration.
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
            // On the first add, create the ThenDeclaration and make the collection start wrapping the EList
            // within the declaration.
            wrapped.setThen(SystemDescriptorFactory.eINSTANCE.createThenDeclaration());
            return wrapped.getThen().getSteps();
          });
    } else {
      // Otherwise, just wrap the steps that are in the existing declaration.
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

  /**
   * Works the same as {@code AutoWrappingCollection} but it calls the provided {@link Supplier} to change the backing
   * list before the first element is added.
   */
  private static class ScenarioInitializingCollection<X extends EObject, T> extends AutoWrappingCollection<X, T> {

    /**
     * The supplier that will supply the list to wrap when the first element is added to this list.
     */
    private final Supplier<EList<X>> initializer;
    /**
     * If true, the first element has been added to this list and this list is now wrapping the list returned from the
     * supplier.
     */
    private boolean hasScenarioBeenInitialized = false;

    /**
     * @param wrapperFunction   the function that converts elements from the wrapped list to elements of type T
     * @param unwrapperFunction the function that converts elements of type T to elements that can be inserted in the
     *                          wrapped list
     * @param initializer       the supplier that is called to get an {@code EList} before the first element is added.
     *                          This collection will wrap the returned list.
     */
    private ScenarioInitializingCollection(Function<X, T> wrapperFunction,
                                           Function<T, X> unwrapperFunction,
                                           Supplier<EList<X>> initializer) {
      // Just past an empty list to the super class for now.  We'll replace it before the first add so it
      // will never actually contain anything.
      super(ECollections.emptyEList(), wrapperFunction, unwrapperFunction);
      this.initializer = initializer;
    }

    @Override
    public boolean add(T t) {
      // Is this the first add?
      if (!hasScenarioBeenInitialized) {
        hasScenarioBeenInitialized = true;
        // Start wrapping the supplied  list.
        setWrapped(initializer.get());
      }
      return super.add(t);
    }
  }
}
