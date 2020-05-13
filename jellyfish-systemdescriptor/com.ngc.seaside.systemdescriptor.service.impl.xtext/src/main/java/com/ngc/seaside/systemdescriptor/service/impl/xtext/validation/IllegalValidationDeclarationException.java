/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
