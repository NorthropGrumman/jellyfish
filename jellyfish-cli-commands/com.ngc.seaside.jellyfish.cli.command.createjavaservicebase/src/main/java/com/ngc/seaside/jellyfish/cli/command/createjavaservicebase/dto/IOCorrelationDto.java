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