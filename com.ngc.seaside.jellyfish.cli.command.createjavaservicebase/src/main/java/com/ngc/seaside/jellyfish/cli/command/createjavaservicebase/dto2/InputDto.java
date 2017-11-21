package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto2;

public class InputDto {
   private String type;
   private String correlationMethod;
   
   public String getType() {
      return type;
   }
   public InputDto setType(String type) {
      this.type = type;
      return this;
   }
   public String getCorrelationMethod() {
      return correlationMethod;
   }
   public InputDto setCorrelationMethod(String correlationMethod) {
      this.correlationMethod = correlationMethod;
      return this;
   }
   
   
}