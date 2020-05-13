/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
public abstract class AbstractWrappedXtext<T extends EObject> implements IUnwrappable<T> {

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

   @Override
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
