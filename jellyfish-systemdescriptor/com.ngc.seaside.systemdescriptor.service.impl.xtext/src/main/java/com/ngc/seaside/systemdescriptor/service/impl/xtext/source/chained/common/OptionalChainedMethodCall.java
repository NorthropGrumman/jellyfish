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

import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.DetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.AbstractChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.location.IDetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.UnknownSourceLocationException;

import java.util.Optional;

/**
 * Chained method call implementation for {@link Optional}.
 * 
 * @param <T> type of optional element
 */
public class OptionalChainedMethodCall<T> extends AbstractChainedMethodCall<Optional<T>> {

   private final ISourceLocation location;
   private final IChainedMethodCall<T> optionalValue;

   /**
    * @param optionalMethodCall returns the chained method call for the {@link Optional#get()} method call, can be
    *           {@code null}
    * @param location location of this optional, can be {@code null}
    * @param context context
    */
   public OptionalChainedMethodCall(IChainedMethodCall<T> optionalMethodCall, ISourceLocation location,
                                    ChainedMethodCallContext context) {
      super(null, context);
      this.optionalValue = optionalMethodCall;
      this.location = location;
      try {
         register(Optional.class.getMethod("get"), this::thenGet);
      } catch (NoSuchMethodException e) {
         throw new AssertionError(e);
      }
   }
   
   /**
    * Creates a chained method call with the location the same as the optional value's location
    * 
    * @param optionalValue returns the chained method call for the {@link Optional#get()} method call, can be
    *           {@code null}
    * @param context context
    */
   public OptionalChainedMethodCall(IChainedMethodCall<T> optionalValue, ChainedMethodCallContext context) {
      this(optionalValue, optionalValue.getLocation(), context);
   }

   private IChainedMethodCall<T> thenGet() {
      if (optionalValue == null) {
         throw new IllegalStateException("Optional is empty");
      }
      return optionalValue;
   }

   @Override
   public IDetailedSourceLocation getLocation() {
      if (location == null) {
         throw new UnknownSourceLocationException("Cannot get source location for optional");
      }
      if (location instanceof IDetailedSourceLocation) {
         return (IDetailedSourceLocation) location;
      } else {
         return new DetailedSourceLocation.Builder(location).build();
      }
   }

}
