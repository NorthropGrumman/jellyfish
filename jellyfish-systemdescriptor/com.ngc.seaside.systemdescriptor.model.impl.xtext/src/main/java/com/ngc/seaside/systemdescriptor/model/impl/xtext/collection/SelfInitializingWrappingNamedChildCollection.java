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

import com.ngc.seaside.systemdescriptor.model.api.INamedChild;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Works the same as {@code WrappedNamedChildCollection} but it calls the provided {@link Supplier} to change the
 * backing list before the first element is added.
 * This class is not threadsafe.
 */
public class SelfInitializingWrappingNamedChildCollection<X extends EObject, P, T extends INamedChild<P>>
      extends WrappingNamedChildCollection<X, P, T> {

   /**
    * The supplier that will supply the list to wrap when the first element is added to this collection.
    */
   private final Supplier<EList<X>> initializer;

   /**
    * If true, the first element has been added to this collection and this list is now wrapping the list returned from
    * the supplier.
    */
   private boolean hasInitialized = false;

   /**
    * Creates a collection.
    *
    * @param wrapperFunction   the function that converts elements from the wrapped list to elements of type T
    * @param unwrapperFunction the function that converts elements of type T to elements that can be inserted in the
    *                          wrapped list
    * @param namingFunction    the function that is used to obtain the name of the elements contained in the wrapped
    * @param initializer       the supplier that is called to get an {@code EList} before the first element is added
    */
   public SelfInitializingWrappingNamedChildCollection(Function<X, T> wrapperFunction,
                                                       Function<T, X> unwrapperFunction,
                                                       Function<X, String> namingFunction,
                                                       Supplier<EList<X>> initializer) {
      // Just past an empty list to the super class for now.  We'll replace it before the first add so it
      // will never actually contain anything.
      super(ECollections.emptyEList(), wrapperFunction, unwrapperFunction, namingFunction);
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
