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
