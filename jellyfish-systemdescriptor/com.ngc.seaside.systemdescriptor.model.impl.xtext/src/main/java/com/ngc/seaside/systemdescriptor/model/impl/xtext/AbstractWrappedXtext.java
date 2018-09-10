/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
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
