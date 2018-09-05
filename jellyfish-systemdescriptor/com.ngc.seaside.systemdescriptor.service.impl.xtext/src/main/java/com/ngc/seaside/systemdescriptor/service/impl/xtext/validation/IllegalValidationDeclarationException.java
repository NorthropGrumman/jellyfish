/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
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
