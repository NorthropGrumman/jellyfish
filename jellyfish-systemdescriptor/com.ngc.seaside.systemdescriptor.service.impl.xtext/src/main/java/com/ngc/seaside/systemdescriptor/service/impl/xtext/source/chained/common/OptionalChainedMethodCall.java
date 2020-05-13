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
