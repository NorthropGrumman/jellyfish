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
