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

import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.UnknownSourceLocationException;

import java.lang.reflect.Executable;
import java.util.function.Function;

/**
 * Chained method call implementation representing a terminal operation; i.e., calls to
 * {@link IChainedMethodCall#then(Executable, Object...)} will fail.
 * 
 * @param <T> any type
 */
public class TerminatingChainedMethodCall<T> implements IChainedMethodCall<T> {

   private final ISourceLocation location;

   public TerminatingChainedMethodCall(ISourceLocation location) {
      this.location = location;
   }

   @Override
   public IChainedMethodCall<?> then(Executable method, Object... arguments) {
      throw new UnknownSourceLocationException("Cannot chain past this method/constructor call");
   }

   @Override
   public <O> IChainedMethodCall<O> calling(Function<? super T, ? extends O> chainedCalls) {
      throw new UnknownSourceLocationException("Cannot chain past this method/constructor call");
   }

   @Override
   public ISourceLocation getLocation() {
      if (location == null) {
         throw new UnknownSourceLocationException();
      }
      return location;
   }

}
