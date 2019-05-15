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

import java.util.Objects;

/**
 * Chained method call implementation for {@link String}. This implementation requires a {@link IDetailedSourceLocation}
 * and uses the text within the source location as the string.
 */
public class StringChainedMethodCall extends AbstractChainedMethodCall<String> {

   private IDetailedSourceLocation location;

   /**
    * @param location location of text
    * @param context context
    */
   public StringChainedMethodCall(IDetailedSourceLocation location, ChainedMethodCallContext context) {
      super(null, context);
      this.location = Objects.requireNonNull(location, "location cannot be null");
      try {
         register(String.class.getMethod("substring", int.class), this::thenSubstring);
         register(String.class.getMethod("substring", int.class, int.class), this::thenSubstring2);
      } catch (NoSuchMethodException e) {
         throw new AssertionError(e);
      }
   }

   private IChainedMethodCall<String> thenSubstring(int index) {
      if (index == 0) {
         return this;
      }
      Preconditions.checkArgument(index >= 0 && index < location.getLength(), "Invalid substring index: " + index);
      IDetailedSourceLocation newLocation = location.getSubLocation(index, location.getLength() - index);
      return new StringChainedMethodCall(newLocation, context);
   }

   private IChainedMethodCall<String> thenSubstring2(int from, int to) {
      Preconditions.checkArgument(from >= 0 && to < location.getLength() && to > from,
               "Invalid substring indices: " + from + ", " + to);
      IDetailedSourceLocation newLocation = location.getSubLocation(from, to - from);
      return new StringChainedMethodCall(newLocation, context);
   }

   @Override
   public IDetailedSourceLocation getLocation() {
      return location;
   }

}
