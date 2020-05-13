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

public class PublishDto {

   private String type;
   private String finalizedType;
   private String name;
   private String topic;
   private String fieldName;
   private String fullyQualifiedName;
   private String correlationSetterSnippet;

   public String getType() {
      return type;
   }

   public PublishDto setType(String type) {
      this.type = type;
      return this;
   }

   public String getName() {
      return name;
   }

   public PublishDto setName(String name) {
      this.name = name;
      return this;
   }

   public String getFieldName() {
      return fieldName;
   }

   public PublishDto setFieldName(String fieldName) {
      this.fieldName = fieldName;
      return this;
   }

   public String getTopic() {
      return topic;
   }

   public PublishDto setTopic(String topic) {
      this.topic = topic;
      return this;
   }

   public PublishDto setFullyQualifiedName(String fullyQualifiedName) {
      this.fullyQualifiedName = fullyQualifiedName;
      return this;
   }

   public String getFullyQualifiedName() {
      return fullyQualifiedName;
   }

   public String getFinalizedType() {
      return finalizedType;
   }

   public PublishDto setFinalizedType(String finalizedType) {
      this.finalizedType = finalizedType;
      return this;
   }

   public String getCorrelationSetterSnippet() {
      return correlationSetterSnippet;
   }

   public PublishDto setCorrelationSetterSnippet(String correlationSetterSnippet) {
      this.correlationSetterSnippet = correlationSetterSnippet;
      return this;
   }
}
