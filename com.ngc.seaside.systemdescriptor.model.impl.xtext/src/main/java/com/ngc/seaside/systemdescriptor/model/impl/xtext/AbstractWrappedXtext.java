package com.ngc.seaside.systemdescriptor.model.impl.xtext;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;

import org.eclipse.emf.ecore.EObject;

import java.util.Objects;

/**
 * Base type of adapters that wrap XText type.
 *
 * @param <T> the type being wrapped
 */
public abstract class AbstractWrappedXtext<T extends EObject> {

  /**
   * The resolver that can resolve existing wrappers for other XText objects.
   */
  protected final IWrapperResolver resolver;

  /**
   * The wrapped object.
   */
  protected final T wrapped;

  protected AbstractWrappedXtext(IWrapperResolver resolver, T wrapped) {
    this.resolver = Preconditions.checkNotNull(resolver, "resolver may not be null!");
    this.wrapped = Preconditions.checkNotNull(wrapped, "wrapped may not be null!");
  }

  /**
   * Gets the XText object that is being wrapped.
   */
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
