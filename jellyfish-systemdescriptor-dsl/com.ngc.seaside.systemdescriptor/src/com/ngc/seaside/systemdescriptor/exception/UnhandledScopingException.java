package com.ngc.seaside.systemdescriptor.exception;

/**
 * A type of exception that indicates an internal error occurred while computing the scope
 * for some element or object.  This type of exception indicates an implementation error
 * as opposed to an invalid System Descriptor file.
 */
public class UnhandledScopingException extends RuntimeException {

   public UnhandledScopingException() {
      super();
   }

   public UnhandledScopingException(String message, Throwable cause, boolean enableSuppression,
                                    boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
   }

   public UnhandledScopingException(String message, Throwable cause) {
      super(message, cause);
   }

   public UnhandledScopingException(String message) {
      super(message);
   }

   public UnhandledScopingException(Throwable cause) {
      super(cause);
   }
}
