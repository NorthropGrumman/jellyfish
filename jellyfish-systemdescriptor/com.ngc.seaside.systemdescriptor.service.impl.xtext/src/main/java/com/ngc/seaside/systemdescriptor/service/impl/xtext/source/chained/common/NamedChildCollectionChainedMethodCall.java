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
package com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common;

import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.location.IDetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.UnknownSourceLocationException;

import java.util.Optional;
import java.util.function.Function;

/**
 * Chained method call implementation for {@link INamedChildCollection}.
 * 
 * @param <T> type of elements in the collection
 * @param <P> parent type of elements
 */
public class NamedChildCollectionChainedMethodCall<P, T extends INamedChild<P>>
         extends CollectionChainedMethodCall<T, INamedChildCollection<P, T>> {

   private final INamedChildCollection<P, T> collection;
   private final Function<? super T, ? extends IChainedMethodCall<T>> fcn;

   /**
    * 
    * @param location source location for entire collection
    * @param collection collection
    * @param fcn function for getting the chained method call of an individual element
    * @param context context
    */
   public NamedChildCollectionChainedMethodCall(IDetailedSourceLocation location,
                                                INamedChildCollection<P, T> collection,
                                                Function<? super T, ? extends IChainedMethodCall<T>> fcn,
                                                ChainedMethodCallContext context) {
      super(location, collection, fcn, context);
      this.collection = collection;
      this.fcn = fcn;
      try {
         register(INamedChildCollection.class.getMethod("getByName", String.class), this::thenGetByName);
      } catch (NoSuchMethodException e) {
         throw new AssertionError(e);
      }
   }

   private IChainedMethodCall<Optional<T>> thenGetByName(String name) {
      T element = collection.getByName(name)
               .orElseThrow(() -> new UnknownSourceLocationException("There is no child named " + name));
      IChainedMethodCall<T> methodCall = fcn.apply(element);
      ISourceLocation location = methodCall.getLocation();
      return new OptionalChainedMethodCall<>(methodCall, location, context);
   }

}
