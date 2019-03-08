/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.systemdescriptor.model.impl.xtext.collection;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.impl.xtext.IUnwrappable;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

/**
 * A collection view on top of an {@code EList} that can automatically adapt instances within the wrapped list to
 * instances of type T and vice versa.
 * This class is not threadsafe.
 *
 * @param <X> the type of object that the wrapped list contains
 * @param <T> the type of object that the contents of the wrapped list should be bridged to
 */
public class AutoWrappingCollection<X extends EObject, T> implements Collection<T> {

   /**
    * A default unwrapper function that can unwrap {@code IUnwrappable} instances to their {@code EObject} types.
    */
   private static final Function DEFAULT_UNWRAPPER = o -> {
      Preconditions.checkArgument(o instanceof IUnwrappable,
                                  "%s is not an instance of %s!",
                                  o,
                                  IUnwrappable.class.getName());
      return ((IUnwrappable) o).unwrap();
   };

   /**
    * The function that adapts or wraps an element in the wrapped list to an element of type T.
    */
   protected final Function<X, T> wrapperFunction;

   /**
    * The function that adapts or unwraps an element of type T to an element that can be inserted into the wrapped list.
    */
   protected final Function<T, X> unwrapperFunction;

   /**
    * The list that is being wrapped.
    */
   protected EList<X> wrapped;

   /**
    * Creates a new {@code AutoWrappingCollection} that is backed by the given list.
    *
    * @param wrapped           the list to wrap
    * @param wrapperFunction   the function that converts elements from the wrapped list to elements of type T
    * @param unwrapperFunction the function that converts elements of type T to elements that can be inserted in the
    *                          wrapped list
    */
   public AutoWrappingCollection(EList<X> wrapped,
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
      return wrapped.contains(unwrapped);
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
      for (X obj : wrapped) {
         array[i++] = wrapperFunction.apply(obj);
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
      for (X obj : wrapped) {
         a[i++] = (T1) wrapperFunction.apply(obj);
      }
      while (i < a.length - 1) {
         a[i++] = null;
      }
      return a;
   }

   @Override
   public boolean add(T t) {
      Preconditions.checkNotNull(t, "t may not be null!");
      // Unwrap the object and add the result to the list.  We don't store an actual reference to t anymore.
      return wrapped.add(unwrapperFunction.apply(t));
   }

   @SuppressWarnings("unchecked")
   @Override
   public boolean remove(Object o) {
      Preconditions.checkNotNull(o, "o may not be null!");
      // No way to avoid this unsafe cast, sorry.
      X unwrapped = unwrapperFunction.apply((T) o);
      return wrapped.remove(unwrapped);
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

   /**
    * Return a default unwrapper function that can be used for generic {@code Object}s provided the input is an
    * {@code IUnwrappable}.
    *
    * @param <A> the type of the input
    * @param <B> the type of the output
    * @return a default unwrapper function
    */
   public static <A, B> Function<A, B> defaultUnwrapper() {
      return DEFAULT_UNWRAPPER;
   }

   /**
    * Sets the {@code EList} that this collection is wrapping.
    *
    * @param toWrap the list to wrap
    */
   protected void setWrapped(EList<X> toWrap) {
      this.wrapped = Preconditions.checkNotNull(toWrap, "toWrap may not be null!");
   }

   /**
    * An iterator that does wrapping and unwrapping.
    */
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
