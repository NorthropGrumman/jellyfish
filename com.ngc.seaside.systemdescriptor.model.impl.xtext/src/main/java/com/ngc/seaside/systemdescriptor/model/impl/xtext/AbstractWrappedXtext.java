package com.ngc.seaside.systemdescriptor.model.impl.xtext;

import com.google.common.base.Preconditions;

import org.eclipse.emf.ecore.EObject;

import java.util.Objects;

public abstract class AbstractWrappedXtext<T extends EObject> {

  protected final T wrapped;

  protected AbstractWrappedXtext(T wrapped) {
    this.wrapped = Preconditions.checkNotNull(wrapped, "wrapped may not be null!");
  }

  public T unwrap() {
    return wrapped;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AbstractWrappedXtext)) {
      return false;
    }
    AbstractWrappedXtext<?> that = (AbstractWrappedXtext<?>) o;
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
}
