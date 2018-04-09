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
