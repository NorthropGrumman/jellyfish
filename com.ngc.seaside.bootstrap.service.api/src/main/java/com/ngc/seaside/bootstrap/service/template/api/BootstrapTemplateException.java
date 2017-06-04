package com.ngc.seaside.bootstrap.service.template.api;

/**
 * Exception to throw when a script would normally exit
 */
public class BootstrapTemplateException extends RuntimeException {

   /**
    * Default constructor with default exit code.
    */
   public BootstrapTemplateException() {
      super();
   }

   /**
    * Error exception constructor with a message.
    *
    * @param message the exception description
    */
   public BootstrapTemplateException(String message) {
      super(message);
   }

   /**
    * Error exception constructor with a message and cause.
    *
    * @param message the exception description
    */
   public BootstrapTemplateException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * Error exception constructor with the cause.
    *
    * @param cause the exception
    */
   public BootstrapTemplateException(Throwable cause) {
      super(cause);
   }
}
