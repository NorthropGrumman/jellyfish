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
