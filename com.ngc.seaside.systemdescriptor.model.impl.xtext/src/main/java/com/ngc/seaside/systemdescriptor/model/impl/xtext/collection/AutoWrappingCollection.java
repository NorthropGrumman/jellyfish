package com.ngc.seaside.systemdescriptor.model.impl.xtext.collection;

import com.google.common.base.Preconditions;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

public abstract class AutoWrappingCollection<X extends EObject, T> implements Collection<T> {

  protected final EList<X> wrapped;
  protected final Function<X, T> wrapperFunction;
  protected final Function<T, X> unwrapperFunction;

  protected AutoWrappingCollection(EList<X> wrapped,
                                   Function<X, T> wrapperFunction,
                                   Function<T, X> unwrapperFunction) {
    this.wrapped = Preconditions.checkNotNull(wrapped, "wrapped may not be null!");
    this.wrapperFunction = Preconditions.checkNotNull(wrapperFunction, "wrapperFunction may not be null!");
    this.unwrapperFunction = Preconditions.checkNotNull(unwrapperFunction, "unwrapperFunction may not be null!");
  }

  @Override
  public int size() {
    return wrapped.size();
  }

  @Override
  public boolean isEmpty() {
    return wrapped.isEmpty();
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean contains(Object o) {
    Preconditions.checkNotNull(o, "o may not be null!");
    // No way to avoid this unsafe cast, sorry.
    X unwrapped = unwrapperFunction.apply((T) o);
    // Equals is not implemented correctly in the Xtext objects, so we have to manually traverse the list.
    return wrapped.stream().anyMatch(x -> EcoreUtil.equals(x, unwrapped));
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
    // No way to avoid this unsafe cast, sorry.
    X unwrapped = unwrapperFunction.apply((T) o);
    // Equals is not implemented correctly in the Xtext objects, so we have to manually traverse the list.
    return wrapped.removeIf(x -> EcoreUtil.equals(x, unwrapped));
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AutoWrappingCollection)) {
      return false;
    }
    AutoWrappingCollection<?, ?> that = (AutoWrappingCollection<?, ?>) o;
    return Objects.equals(wrapped, that.wrapped);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wrapped);
  }

  @Override
  public String toString() {
    return wrapped.toString();
  }

  protected class WrappingIterator implements Iterator<T> {

    protected final Iterator<X> wrappedIterator;

    protected WrappingIterator(Iterator<X> wrappedIterator) {
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
