package com.ngc.seaside.systemdescriptor.model.impl.xtext;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;

public class WrappedNamedChildCollection<X extends EObject, P, T extends INamedChild<P>>
    implements INamedChildCollection<P, T> {

  private final EList<X> wrapped;
  private final Function<X, T> wrapperFunction;
  private final Function<T, X> unwrapperFunction;
  private final Function<X, String> namingFunction;

  public WrappedNamedChildCollection(EList<X> wrapped,
                                     Function<X, T> wrapperFunction,
                                     Function<T, X> unwrapperFunction,
                                     Function<X, String> namingFunction) {
    this.wrapped = Preconditions.checkNotNull(wrapped, "wrapped may not be null!");
    this.wrapperFunction = Preconditions.checkNotNull(wrapperFunction, "wrapperFunction may not be null!");
    this.unwrapperFunction = Preconditions.checkNotNull(unwrapperFunction, "unwrapperFunction may not be null!");
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
  public int size() {
    return wrapped.size();
  }

  @Override
  public boolean isEmpty() {
    return wrapped.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    boolean contains = o instanceof INamedChild;
    if (contains) {
      contains = getByName(((INamedChild<?>) o).getName()).isPresent();
    }
    return contains;
  }

  @Override
  public Iterator<T> iterator() {
    return new WrappingIterator(wrapped.iterator());
  }

  @Override
  public Object[] toArray() {
    // Thread safety note: this isn't thread safe if the list is being modified.
    Object[] array = new Object[size()];
    int i = 0;
    for (X aWrapped : wrapped) {
      array[i++] = wrapperFunction.apply(aWrapped);
    }
    return array;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T1> T1[] toArray(T1[] a) {
    // Thread safety note: this isn't thread safe if the list is being modified.
    if (a.length < size()) {
      a = (T1[]) java.lang.reflect.Array.newInstance(
          a.getClass().getComponentType(), size());
    }

    int i = 0;
    for (X aWrapped : wrapped) {
      a[i++] = (T1) wrapperFunction.apply(aWrapped);
    }
    while (i < a.length - 1) {
      a[i++] = null;
    }
    return a;
  }

  @Override
  public boolean add(T t) {
    Preconditions.checkNotNull(t, "t may not be null!");
    return wrapped.add(unwrapperFunction.apply(t));
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean remove(Object o) {
    Preconditions.checkNotNull(o, "o may not be null!");
    boolean removed = o instanceof INamedChild;
    if(removed) {
      String name = ((INamedChild<?>) o).getName();
      Optional<X> xtextChild = wrapped.stream()
          .filter(x -> namingFunction.apply(x).equals(name))
          .findFirst();
      removed = xtextChild.isPresent();
      if(removed) {
        wrapped.remove(xtextChild.get());
      }
    }
    return removed;
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    Preconditions.checkNotNull(c, "c may not be null!");
    boolean contains = true;
    for (Object o : c) {
      contains &= contains(o);
    }
    return contains;
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    Preconditions.checkNotNull(c, "c may not be null!");
    boolean added = false;
    for (T t : c) {
      added |= add(t);
    }
    return added;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    Preconditions.checkNotNull(c, "c may not be null!");
    boolean removed = false;
    for (Object o : c) {
      removed |= remove(o);
    }
    return removed;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    Preconditions.checkNotNull(c, "c may not be null!");
    boolean changed = false;
    for (Object o : c) {
      if (!contains(o)) {
        changed |= remove(o);
      }
    }
    return changed;
  }

  @Override
  public void clear() {
    wrapped.clear();
  }

  private class WrappingIterator implements Iterator<T> {

    private final Iterator<X> wrappedIterator;

    private WrappingIterator(Iterator<X> wrappedIterator) {
      this.wrappedIterator = wrappedIterator;
    }

    @Override
    public boolean hasNext() {
      return wrappedIterator.hasNext();
    }

    @Override
    public T next() {
      return wrapperFunction.apply(wrappedIterator.next());
    }

    @Override
    public void remove() {
      wrappedIterator.remove();
    }
  }
}
