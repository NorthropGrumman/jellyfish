package com.ngc.seaside.jellyfish.service.template.api;

/**
 * The exception thrown by any implementation of the ITemplateService.
 */
public class TemplateServiceException extends RuntimeException {

   /**
    * Default constructor with default exit code.
    */
   public TemplateServiceException() {
      super();
   }

   /**
    * Error exception constructor with a message.
    *
    * @param message the exception description
    */
   public TemplateServiceException(String message) {
      super(message);
   }

   /**
    * Error exception constructor with a message and cause.
    *
    * @param message the exception description
    */
   public TemplateServiceException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * Error exception constructor with the cause.
    *
    * @param cause the exception
    */
   public TemplateServiceException(Throwable cause) {
      super(cause);
   }
}
