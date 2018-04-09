package com.ngc.seaside.systemdescriptor.service.impl.xtext.validation;

/**
 * A type of exception that may be thrown when some
 * {@link com.ngc.seaside.systemdescriptor.validation.api.ISystemDescriptorValidator ISystemDescriptorValidator}
 * attempts to declare some kind of validation issue on a field or structural feature that is not valid.  In most cases,
 * this means the validator attempted to declare an issue on a readonly or derived property or or some object which is
 * not object being actively validated.
 */
public class IllegalValidationDeclarationException extends RuntimeException {

   /**
    * Creates a new exception.
    */
   public IllegalValidationDeclarationException() {
   }

   /**
    * Creates a new exception.
    */
   public IllegalValidationDeclarationException(String message) {
      super(message);
   }

   /**
    * Creates a new exception.
    */
   public IllegalValidationDeclarationException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * Creates a new exception.
    */
   public IllegalValidationDeclarationException(Throwable cause) {
      super(cause);
   }

   /**
    * Creates a new exception.
    */
   public IllegalValidationDeclarationException(String message,
                                                Throwable cause,
                                                boolean enableSuppression,
                                                boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
   }
}
