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
