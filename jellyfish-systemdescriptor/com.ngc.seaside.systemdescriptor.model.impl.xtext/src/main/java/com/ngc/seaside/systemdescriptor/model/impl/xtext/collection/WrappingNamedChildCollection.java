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
