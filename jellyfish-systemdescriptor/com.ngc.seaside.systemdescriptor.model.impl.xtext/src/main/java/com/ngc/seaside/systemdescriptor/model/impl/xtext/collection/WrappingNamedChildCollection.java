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
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import java.util.Optional;
import java.util.function.Function;

/**
 * A type of auto wrapping collection that also implements {@code INamedChildCollection}.
 * This class is not threadsafe.
 *
 * @param <X> the type of {@code EObject} that is contained in the wrapped list
 * @param <P> the parent of the {@code INamedChild}
 * @param <T> the type that the elements in the wrapped list are being bridged to
 */
public class WrappingNamedChildCollection<X extends EObject, P, T extends INamedChild<P>>
      extends AutoWrappingCollection<X, T>
      implements INamedChildCollection<P, T> {

   /**
    * Gets the names of elements within the wrapped list.
    */
   private final Function<X, String> namingFunction;

   /**
    * Creates a collection.
    *
    * @param wrapped           the {@code EList} to wrap
    * @param wrapperFunction   the function that converts elements from the wrapped list to elements of type T
    * @param unwrapperFunction the function that converts elements of type T to elements that can be inserted in the
    *                          wrapped list
    * @param namingFunction    the function that is used to obtain the name of the elements contained in the wrapped
    *                          list
    */
   public WrappingNamedChildCollection(EList<X> wrapped,
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

      // Find the element in the wrapped list with the given name.  Use the function to determine the names of the
      // elements in the wrapped list.
      Optional<X> xtextChild = wrapped.stream()
            .filter(x -> namingFunction.apply(x).equals(name))
            .findFirst();
      return xtextChild.map(wrapperFunction);
   }
}
