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

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.AbstractChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.location.IDetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.UnknownSourceLocationException;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;

/**
 * Chained method call implementation for {@link Collection} and its sub-classes.
 * 
 * @param <T> type of elements in the collection
 * @param <C> collection type
 */
public class CollectionChainedMethodCall<T, C extends Collection<T>> extends AbstractChainedMethodCall<C> {

   private final List<Entry<T, IChainedMethodCall<T>>> methodCalls;
   private final IDetailedSourceLocation location;

   /**
    * @param location location of entire collection, can be null
    * @param methodCalls elements of collection their corresponding chained method call
    * @param context context
    */
   public CollectionChainedMethodCall(IDetailedSourceLocation location,
                                      C collection,
                                      Collection<Entry<T, IChainedMethodCall<T>>> methodCalls,
                                      ChainedMethodCallContext context) {
      super(collection, context);
      this.methodCalls = new ArrayList<>(methodCalls);
      this.location = location;
      try {
         register(Iterable.class.getMethod("iterator"), this::thenIterator);
         register(Collection.class.getMethod("contains", Object.class),
                  (OneParameterChainedMethodCallIntercept<Boolean, Object>) this::thenFind);
         register(List.class.getMethod("get", int.class), this::thenGet);
         register(List.class.getMethod("subList", int.class, int.class), this::thenSubList);
         register(List.class.getMethod("indexOf", Object.class),
                  (OneParameterChainedMethodCallIntercept<Boolean, Object>) this::thenFind);
         register(ArrayList.class.getConstructor(Collection.class), this::thenNewArrayList);
      } catch (NoSuchMethodException e) {
         throw new AssertionError(e);
      }
   }

   /**
    * @param location location of entire collection, can be null
    * @param elements elements of collection
    * @param fcn function to map an element to its chained method call
    * @param context context
    */
   public CollectionChainedMethodCall(IDetailedSourceLocation location, C elements,
                                      Function<? super T, ? extends IChainedMethodCall<T>> fcn,
                                      ChainedMethodCallContext context) {
      this(location, elements, getAsList(elements, fcn), context);
   }

   private IChainedMethodCall<T> thenGet(int index) {
      Preconditions.checkArgument(index >= 0 && index < methodCalls.size(), "Invalid index: " + index);
      return methodCalls.get(index).getValue();
   }

   private IChainedMethodCall<List<T>> thenSubList(int from, int to) {
      Preconditions.checkArgument(from >= 0 && to <= methodCalls.size() && to > from,
               "Invalid sublist indices: " + from + ", " + to);
      return new CollectionChainedMethodCall<>(getSubLocation(from, to), new ArrayList<>(instance),
               methodCalls.subList(from, to), context);
   }

   private <E> IChainedMethodCall<E> thenFind(Object source) {
      for (Entry<T, IChainedMethodCall<T>> entry : methodCalls) {
         if (Objects.equals(entry.getKey(), source)) {
            return new TerminatingChainedMethodCall<>(entry.getValue().getLocation());
         }
      }
      throw new IllegalStateException("Collection does not contain " + source);
   }

   private IChainedMethodCall<Iterator<T>> thenIterator() {
      return new AbstractChainedMethodCall<Iterator<T>>(instance.iterator(), context) {
         private final Iterator<Entry<T, IChainedMethodCall<T>>> iterator;

         {
            this.iterator = methodCalls.iterator();
            try {
               register(Iterator.class.getMethod("next"), this::thenNext);
            } catch (NoSuchMethodException e) {
               throw new AssertionError(e);
            }
         }

         private IChainedMethodCall<T> thenNext() {
            return iterator.next().getValue();
         }

         @Override
         public IDetailedSourceLocation getLocation() {
            throw new IllegalStateException("Cannot get source location of iterator");
         }
      };
   }

   private IChainedMethodCall<?> thenNewArrayList() {
      return this;
   }

   private IDetailedSourceLocation getSubLocation(int from, int to) {
      int min = Integer.MAX_VALUE;
      int max = Integer.MIN_VALUE;
      IDetailedSourceLocation first = null;
      for (Entry<T, IChainedMethodCall<T>> entry : methodCalls.subList(from, to)) {
         IChainedMethodCall<T> element = entry.getValue();
         ISourceLocation location = element.getLocation();
         IDetailedSourceLocation detailedLocation;
         if (location instanceof IDetailedSourceLocation) {
            detailedLocation = (IDetailedSourceLocation) location;
         } else {
            throw new IllegalArgumentException(
                     "Expected " + IDetailedSourceLocation.class.getName() + ": " + location.getClass());
         }
         if (first == null) {
            first = detailedLocation;
         }
         min = Math.min(min, detailedLocation.getOffset());
         max = Math.max(max, detailedLocation.getOffset() + location.getLength());
      }
      return first.getSubLocation(min - first.getOffset(), max - min);
   }

   @Override
   public IDetailedSourceLocation getLocation() {
      if (location == null) {
         throw new UnknownSourceLocationException();
      }
      return location;
   }

   private static <T> List<Entry<T, IChainedMethodCall<T>>> getAsList(
            Collection<T> collection, Function<? super T, ? extends IChainedMethodCall<T>> fcn) {
      List<Entry<T, IChainedMethodCall<T>>> list = new ArrayList<>(collection.size());
      for (T element : collection) {
         Entry<T, IChainedMethodCall<T>> entry = new SimpleImmutableEntry<>(element, fcn.apply(element));
         list.add(entry);
      }
      return list;
   }
}
