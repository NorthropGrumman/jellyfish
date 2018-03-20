package com.ngc.seaside.systemdescriptor.errorhandling;

import com.google.common.base.Preconditions;

import org.eclipse.xtext.util.PolymorphicDispatcher.ErrorHandler;

import java.util.function.Function;

public class ExceptionRethrowingErrorHandler<T> implements ErrorHandler<T> {

   private final Function<Throwable, RuntimeException> exceptionProducer;

   public ExceptionRethrowingErrorHandler(Function<Throwable, RuntimeException> exceptionProducer) {
      this.exceptionProducer = Preconditions.checkNotNull(exceptionProducer, "exceptionProducer may not be null!");
   }

   @Override
   public T handle(Object[] params, Throwable throwable) {
      throw exceptionProducer.apply(throwable);
   }

}
