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