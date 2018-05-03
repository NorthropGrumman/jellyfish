package com.ngc.seaside.jellyfish.service.execution.api;

/**
 * A generic exception that may occur during the execution of Jellyfish.  This type of exception is typically not raised
 * for a parsing error.  Instead, it may indicate a misconfiguration or deeper problem.
 */
public class JellyfishExecutionException extends RuntimeException {

   /**
    * Creates a new exception.
    */
   public JellyfishExecutionException() {
   }

   /**
    * Creates a new exception.
    */
   public JellyfishExecutionException(String message) {
      super(message);
   }

   /**
    * Creates a new exception.
    */
   public JellyfishExecutionException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * Creates a new exception.
    */
   public JellyfishExecutionException(Throwable cause) {
      super(cause);
   }

   /**
    * Creates a new exception.
    */
   public JellyfishExecutionException(String message, Throwable cause, boolean enableSuppression,
                                      boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
   }
}
