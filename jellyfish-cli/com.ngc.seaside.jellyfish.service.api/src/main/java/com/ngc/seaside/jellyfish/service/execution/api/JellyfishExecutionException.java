/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish.service.execution.api;

/**
 * A generic exception that may occur during the execution of Jellyfish.  This type of exception is typically not raised
 * for a parsing error.  Instead, it may indicate a misconfiguration or deeper problem.
 */
public class JellyfishExecutionException extends RuntimeException {

   /**
    * Creates a new exception.
    */
   public JellyfishExecutionException() {
   }

   /**
    * Creates a new exception.
    */
   public JellyfishExecutionException(String message) {
      super(message);
   }

   /**
    * Creates a new exception.
    */
   public JellyfishExecutionException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * Creates a new exception.
    */
   public JellyfishExecutionException(Throwable cause) {
      super(cause);
   }

   /**
    * Creates a new exception.
    */
   public JellyfishExecutionException(String message, Throwable cause, boolean enableSuppression,
                                      boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
   }
}
