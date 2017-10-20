/**
***************************************** UNCLASSIFIED ******************************************
*************************************************************************************************
************************* Copyright 2017 Northrop Grumman Corporation ***************************
* Author: Generated
*/
package com.ngc.example.inhertance.databagsservice.event.datatype;

/**
 * This file has been generated and should not be edited directly.
 * @author Generated
 */
public class B extends com.ngc.example.inhertance.databagsservice.event.datatype.A {

   public static final String TOPIC_NAME = "/data/com.ngc.example.inhertance.databagsservice.event.datatype.B";

   public static final com.ngc.blocs.service.event.api.IEventTopic<B> TOPIC = 
      com.ngc.blocs.service.event.api.EventTopic.of(TOPIC_NAME, B.class);

   private int fieldB1;
   private com.ngc.example.inhertance.databagsservice.event.datatype.C fieldB2;

   public int getFieldB1() {
      return fieldB1;
   }

   public B setFieldB1(int fieldB1) {
      this.fieldB1 = fieldB1;
      return this;
   }

   public com.ngc.example.inhertance.databagsservice.event.datatype.C getFieldB2() {
      return fieldB2;
   }

   public B setFieldB2(com.ngc.example.inhertance.databagsservice.event.datatype.C fieldB2) {
      this.fieldB2 = fieldB2;
      return this;
   }

   @Override
   public int hashCode() {
      return java.util.Objects.hash(
      super.hashCode(),         this.fieldB1,         this.fieldB2      );
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == this) {
         return true;
      }

      if(!(obj instanceof B)) {
         return false;
      }

      B that = (B) obj;

      return super.equals(obj) &&         java.util.Objects.equals(this.fieldB1, that.fieldB1) &&         java.util.Objects.equals(this.fieldB2, that.fieldB2)      ;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(super.toString());
      builder.append("[fieldB1=").append(fieldB1).append("]");
      builder.append("[fieldB2=").append(fieldB2).append("]");
      return builder.toString();
   }

}
