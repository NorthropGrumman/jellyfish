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
package com.ngc.seaside.systemdescriptor.model.impl.xtext.collection;

import com.google.common.base.Preconditions;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Works the same as {@code AutoWrappingCollection} but it calls the provided {@link Supplier} to change the backing
 * list before the first element is added.
 * This class is not threadsafe.
 */
public class SelfInitializingAutoWrappingCollection<X extends EObject, T> extends AutoWrappingCollection<X, T> {

   /**
    * The supplier that will supply the list to wrap when the first element is added to this list.
    */
   private final Supplier<EList<X>> initializer;

   /**
    * If true, the first element has been added to this list and this list is now wrapping the list returned from the
    * supplier.
    */
   private boolean hasInitialized = false;

   /**
    * @param wrapperFunction   the function that converts elements from the wrapped list to elements of type T
    * @param unwrapperFunction the function that converts elements of type T to elements that can be inserted in the
    *                          wrapped list
    * @param initializer       the supplier that is called to get an {@code EList} before the first element is added.
    *                          This collection will wrap the returned list.
    */
   public SelfInitializingAutoWrappingCollection(Function<X, T> wrapperFunction,
                                                 Function<T, X> unwrapperFunction,
                                                 Supplier<EList<X>> initializer) {
      // Just past an empty list to the super class for now.  We'll replace it before the first add so it
      // will never actually contain anything.
      super(ECollections.emptyEList(), wrapperFunction, unwrapperFunction);
      this.initializer = Preconditions.checkNotNull(initializer, "initializer may not be null!");
   }

   @Override
   public boolean add(T t) {
      // Is this the first add?
      if (!hasInitialized) {
         hasInitialized = true;
         // Start wrapping the supplied  list.
         setWrapped(initializer.get());
      }
      return super.add(t);
   }
}
