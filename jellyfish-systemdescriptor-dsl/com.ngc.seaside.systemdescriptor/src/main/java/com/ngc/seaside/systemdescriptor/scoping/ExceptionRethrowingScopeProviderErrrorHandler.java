/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
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
