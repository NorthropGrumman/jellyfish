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

public class InputDto {
   private String type;
   private String correlationMethod;
   private String fieldName;
   private String fullyQualifiedName;

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
   
   public String getFieldName() {
      return fieldName;
   }

   public InputDto setFieldName(String fieldName) {
      this.fieldName = fieldName;
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

   public InputDto setFullyQualifiedName(String fullyQualifiedName) {
      this.fullyQualifiedName = fullyQualifiedName;
      return this;
   }

   public  String getFullyQualifiedName() {
      return fullyQualifiedName;
   }
}
