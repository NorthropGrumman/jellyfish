package com.ngc.seaside.systemdescriptor.model.impl.xtext.collection;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import java.util.Optional;
import java.util.function.Function;

public class WrappedNamedChildCollection<X extends EObject, P, T extends INamedChild<P>>
    extends AutoWrappingCollection<X, T>
    implements INamedChildCollection<P, T> {

  private final Function<X, String> namingFunction;

  public WrappedNamedChildCollection(EList<X> wrapped,
                                     Function<X, T> wrapperFunction,
                                     Function<T, X> unwrapperFunction,
                                     Function<X, String> namingFunction) {
    super(wrapped, wrapperFunction, unwrapperFunction);
    this.namingFunction = Preconditions.checkNotNull(namingFunction, "namingFunction may not be null!");
  }

  @Override
  public Optional<T> getByName(String name) {
    Preconditions.checkNotNull(name, "name may not be null!");
    Preconditions.checkArgument(!name.trim().isEmpty(), "name may not be empty!");
    Optional<X> xtextChild = wrapped.stream()
        .filter(x -> namingFunction.apply(x).equals(name))
        .findFirst();
    return xtextChild.map(wrapperFunction);
  }

  @Override
  public boolean contains(Object o) {
    boolean contains = o instanceof INamedChild;
    if (contains) {
      contains = getByName(((INamedChild<?>) o).getName()).isPresent();
    }
    return contains;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean remove(Object o) {
    Preconditions.checkNotNull(o, "o may not be null!");
    boolean removed = o instanceof INamedChild;
    if (removed) {
      String name = ((INamedChild<?>) o).getName();
      Optional<X> xtextChild = wrapped.stream()
          .filter(x -> namingFunction.apply(x).equals(name))
          .findFirst();
      removed = xtextChild.isPresent();
      if (removed) {
        wrapped.remove(xtextChild.get());
      }
    }
    return removed;
  }
}
