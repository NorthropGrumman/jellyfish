package com.ngc.seaside.jellyfish.service.parameter.api;

/**
 * Any exception in the IPromptUserService implementation.
 */
public class ParameterServiceException extends RuntimeException {

   /**
    * Default constructor with default exit code.
    */
   public ParameterServiceException() {
      super();
   }

   /**
    * Error exception constructor with a message.
    *
    * @param message the exception description
    */
   public ParameterServiceException(String message) {
      super(message);
   }

   /**
    * Error exception constructor with a message and cause.
    *
    * @param message the exception description
    */
   public ParameterServiceException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * Error exception constructor with the cause.
    *
    * @param cause the exception
    */
   public ParameterServiceException(Throwable cause) {
      super(cause);
   }
}
