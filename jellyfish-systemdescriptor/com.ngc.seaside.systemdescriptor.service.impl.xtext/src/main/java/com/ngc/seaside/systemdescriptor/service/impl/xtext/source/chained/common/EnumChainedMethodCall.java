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

import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.AbstractChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.location.IDetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;

import java.util.Objects;

/**
 * Chained method call implementation for enums. This implementation assumes the {@link Enum#name()} is the same as the
 * text in the source location.
 * 
 * @param <T> type of enum
 */
public class EnumChainedMethodCall<T extends Enum<T>> extends AbstractChainedMethodCall<T> {

   private IDetailedSourceLocation location;

   /**
    * @param location location of enum, cannot be null
    * @param context context
    */
   public EnumChainedMethodCall(IDetailedSourceLocation location, ChainedMethodCallContext context) {
      super(null, context);
      this.location = Objects.requireNonNull(location, "location cannot be null");
      try {
         register(Enum.class.getMethod("name"), this::thenName);
         register(Object.class.getMethod("toString"), this::thenName);
      } catch (NoSuchMethodException e) {
         throw new AssertionError(e);
      }
   }

   private IChainedMethodCall<String> thenName() {
      return new StringChainedMethodCall(location, context);
   }

   @Override
   public IDetailedSourceLocation getLocation() {
      return location;
   }

}
