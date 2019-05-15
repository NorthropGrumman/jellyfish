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
package com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained;

import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;

import java.lang.reflect.Executable;
import java.util.function.Function;

public class WrappedChainedMethodCall<T> implements IChainedMethodCall<T> {

   protected final IChainedMethodCall<T> wrapped;

   public WrappedChainedMethodCall(IChainedMethodCall<T> wrapped) {
      this.wrapped = wrapped;
   }

   @Override
   public IChainedMethodCall<?> then(Executable methodOrConstructor, Object... arguments) {
      return wrapped.then(methodOrConstructor, arguments);
   }

   @Override
   public <O> IChainedMethodCall<O> calling(Function<? super T, ? extends O> chainedCalls) {
      return wrapped.calling(chainedCalls);
   }

   @Override
   public ISourceLocation getLocation() {
      return wrapped.getLocation();
   }

}
