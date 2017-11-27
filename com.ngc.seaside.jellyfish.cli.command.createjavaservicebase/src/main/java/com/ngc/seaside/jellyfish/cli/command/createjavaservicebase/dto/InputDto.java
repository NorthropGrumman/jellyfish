package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto;

public class InputDto {
   private String type;
   private String correlationMethod;

   /**
    * Returns the name of the generated java event type for this input.
    */
   public String getType() {
      return type;
   }

   public InputDto setType(String type) {
      this.type = type;
      return this;
   }

   /**
    * Returns the method name that should be called when the correlation involving this input is completed.
    */
   public String getCorrelationMethod() {
      return correlationMethod;
   }

   public InputDto setCorrelationMethod(String correlationMethod) {
      this.correlationMethod = correlationMethod;
      return this;
   }

}
