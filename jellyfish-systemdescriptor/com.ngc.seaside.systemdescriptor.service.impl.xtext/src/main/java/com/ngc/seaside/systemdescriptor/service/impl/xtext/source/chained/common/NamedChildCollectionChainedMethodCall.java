/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
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
