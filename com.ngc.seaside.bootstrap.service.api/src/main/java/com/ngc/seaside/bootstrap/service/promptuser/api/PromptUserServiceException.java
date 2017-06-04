package com.ngc.seaside.bootstrap.service.promptuser.api;

/**
 * Exception to throw when a script would normally exit
 */
public class PromptUserServiceException extends RuntimeException {

   /**
    * Default constructor with default exit code.
    */
   public PromptUserServiceException() {
      super();
   }

   /**
    * Error exception constructor with a message.
    *
    * @param message the exception description
    */
   public PromptUserServiceException(String message) {
      super(message);
   }

   /**
    * Error exception constructor with a message and cause.
    *
    * @param message the exception description
    */
   public PromptUserServiceException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * Error exception constructor with the cause.
    *
    * @param cause the exception
    */
   public PromptUserServiceException(Throwable cause) {
      super(cause);
   }
}
