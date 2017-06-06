package com.ngc.seaside.systemdescriptor.service.impl.xtext.validation;

public class IllegalValidationDeclarationException extends RuntimeException {

   public IllegalValidationDeclarationException() {
   }

   public IllegalValidationDeclarationException(String message) {
      super(message);
   }

   public IllegalValidationDeclarationException(String message, Throwable cause) {
      super(message, cause);
   }

   public IllegalValidationDeclarationException(Throwable cause) {
      super(cause);
   }

   public IllegalValidationDeclarationException(String message,
                                                Throwable cause,
                                                boolean enableSuppression,
                                                boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
   }
}
