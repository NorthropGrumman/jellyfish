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
package com.ngc.seaside.systemdescriptor.scoping;

import com.ngc.seaside.systemdescriptor.exception.UnhandledScopingException;

import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.util.PolymorphicDispatcher.ErrorHandler;

/**
 * A type of error handler that is used by the scope provider.  This error handler
 * will rethrow any important exception as an {@code UnhandledScopingException}.
 * This is important since the default error handler will silently consume any
 * exceptions.
 */
public class ExceptionRethrowingScopeProviderErrrorHandler implements ErrorHandler<IScope> {

   @Override
   public IScope handle(Object[] params, Throwable throwable) {
      // This type of exception can occur if no method is defined to compute scope
      // for a particular rule.  However, this exception is harmless and most of
      // the time the built in scoping will handle.  Thus, it is safe to consume this
      // error.
      if (throwable instanceof NoSuchMethodException) {
         return null;
      }
      // Otherwise, this could be a bug in the scope provider so rethrow it
      // so the bug and exception is visible.
      throw new UnhandledScopingException(throwable.getMessage(), throwable);
   }

}
