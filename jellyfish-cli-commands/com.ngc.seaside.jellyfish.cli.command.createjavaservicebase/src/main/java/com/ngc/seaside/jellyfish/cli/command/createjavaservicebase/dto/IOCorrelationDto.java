package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto;

public class IOCorrelationDto {

   private String getterSnippet;
   private String setterSnippet;
   private String inputType;

   /**
    * Returns the snippet of code used to get the input field that is being correlated.
    * This typically is a chain of getter methods.
    */
   public String getGetterSnippet() {
      return getterSnippet;
   }

   public IOCorrelationDto setGetterSnippet(String getterSnippet) {
      this.getterSnippet = getterSnippet;
      return this;
   }

   /**
    * Returns the snippet of code used to set the output field that is being correlated.
    * This typically is a chain of getter methods following by the name of the setter method.
    */
   public String getSetterSnippet() {
      return setterSnippet;
   }

   public IOCorrelationDto setSetterSnippet(String setterSnippet) {
      this.setterSnippet = setterSnippet;
      return this;
   }

   /**
    * Returns the generated java events type for the input.
    */
   public String getInputType() {
      return inputType;
   }

   public IOCorrelationDto setInputType(String inputType) {
      this.inputType = inputType;
      return this;
   }

}