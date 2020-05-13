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
