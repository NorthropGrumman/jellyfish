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
package com.ngc.seaside.jellyfish.cli.command.analyzebudget.budget;

public class BudgetValidationException extends RuntimeException {

   private final Object source;
   private final String simpleMessage;

   public BudgetValidationException(String message, Throwable cause, Object source) {
      this(message, cause, source, message);
   }

   /**
    * Constructor.
    * 
    * @param message descriptive message
    * @param cause cause, can be null
    * @param source source of exception
    * @param simpleMessage simpler message
    */
   public BudgetValidationException(String message, Throwable cause, Object source, String simpleMessage) {
      super(message, cause);
      this.source = source;
      this.simpleMessage = simpleMessage;
   }

   public Object getSource() {
      return source;
   }

   public String getSimpleMessage() {
      return simpleMessage;
   }

}
