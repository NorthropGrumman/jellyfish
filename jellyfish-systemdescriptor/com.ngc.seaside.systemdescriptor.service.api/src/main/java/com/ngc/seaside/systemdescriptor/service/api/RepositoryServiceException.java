package com.ngc.seaside.systemdescriptor.service.api;

/**
 * Any exception in the IRepositoryService implementation.
 */
public class RepositoryServiceException extends RuntimeException {
   /**
    * Default constructor with default exit code.
    */
   public RepositoryServiceException() {
      super();
   }

   /**
    * Error exception constructor with a message.
    *
    * @param message the exception description
    */
   public RepositoryServiceException(String message) {
      super(message);
   }

   /**
    * Error exception constructor with a message and cause.
    *
    * @param message the exception description
    */
   public RepositoryServiceException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * Error exception constructor with the cause.
    *
    * @param cause the exception
    */
   public RepositoryServiceException(Throwable cause) {
      super(cause);
   }
}
